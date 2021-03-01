/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.data

import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.BabyChangingStation
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EmojiNature
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Pages
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Transgender
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.androiddevchallenge.ui.details.emailIntent
import com.example.androiddevchallenge.ui.details.mapIntent
import com.example.androiddevchallenge.ui.details.phoneIntent
import com.google.gson.Gson
import java.text.DecimalFormat

data class AnimalPhoto(
    val small: String,
    val medium: String,
    val large: String,
    val full: String
)

data class AnimalVideo(val embed: String)

data class AnimalPagination(
    val count_per_page: Int,
    val total_count: Int,
    val current_page: Int,
    val total_pages: Int
)

data class Breeds(
    val primary: String,
    val secondary: String?,
    val mixed: Boolean,
    val unknown: Boolean
)

data class AnimalColors(
    val primary: String,
    val secondary: String?,
    val tertiary: String?
)

data class AnimalsResponse(val animals: List<Animal>, val pagination: AnimalPagination)

data class Animal(
    val id: String,
    val organization_id: String,
    val url: String,
    val type: String,
    val species: String,
    val breeds: Breeds,
    val colors: AnimalColors,
    val age: String,
    val gender: String,
    val size: String,
    val coat: String?,
    val name: String,
    val description: String? = "",
    val primary_photo_cropped: AnimalPhoto?,
    val photos: List<AnimalPhoto>,
    val videos: List<AnimalVideo>,
    val status: String,
    val attributes: AnimalAttributes,
    val environment: AnimalEnvironment,
    val tags: List<String>,
    val contact: AnimalContact,
    val published_at: String,
    val distance: Float,
)

data class AnimalDetailsRow(val title: String, val icon: ImageVector, val openIntent: Intent? = null)

fun Animal.nameAndDistance(): String {
    return "${this.name} - ${ normalizedDistance() }  miles away"
}

fun Animal.normalizedDistance(): String {
    val df = DecimalFormat("#.##")
    return "${df.format(this.distance) }"
}

fun Animal.toDetailList(): List<AnimalDetailsRow> {

    val coat = this.coat?.let { AnimalDetailsRow("Coat: ${this.coat}", Icons.Default.Pages) }
    // TODO -  create view, that shows the embed video of the pet
//    val video = this.videos.firstOrNull()?.let{
//        val intent = Intent(Intent.ACTION_VIEW);
//        intent.data = Uri.parse(this.videos.first().embed);
//        AnimalDetailsRow("Cute video of the animal", Icons.Default.FeaturedVideo, openIntent = intent)
//    }
    val colorsValues = this.colors.toDescription()
    val colors = if (colorsValues.isNotEmpty()) {
        AnimalDetailsRow("Color: $colorsValues", Icons.Default.Colorize)
    } else {
        null
    }

    val environmentValues = this.environment.toDescription()
    val environment = if (environmentValues.isNotEmpty()) {
        AnimalDetailsRow("Environment: $environmentValues", Icons.Default.EmojiNature)
    } else {
        null
    }
    return listOfNotNull(
        colors,
        AnimalDetailsRow("Age: ${this.age}", Icons.Default.BabyChangingStation),
        AnimalDetailsRow("Gender: ${this.gender}", Icons.Default.Transgender),
        AnimalDetailsRow("Breeds: ${this.breeds.toDescription()}", Icons.Default.Public),
        AnimalDetailsRow("Size: ${this.size}", Icons.Default.Leaderboard),
        AnimalDetailsRow("Species: ${this.species}", Icons.Default.Api),
        coat,
        AnimalDetailsRow("Attributes: ${this.attributes.toDescription()}", Icons.Default.Article),
        environment,
        // video
    )
}

fun Animal.publishedDate(): String {
    return published_at.substring(0, published_at.indexOf("T"))
}

fun Animal.toContactList(): List<AnimalDetailsRow> {

    val phone = this.contact.phone?.let {
        AnimalDetailsRow(
            "Phone: ${this.contact.phone}",
            Icons.Default.Phone,
            this.contact.phone.phoneIntent()
        )
    }
    val email = this.contact.email?.let {
        AnimalDetailsRow(
            "Email: ${this.contact.email}",
            Icons.Default.Email,
            this.contact.email.emailIntent(this.name)
        )
    }

    return listOfNotNull(
        phone,
        email,
        AnimalDetailsRow(
            "Address: ${this.contact.address.toReadableAddress() }",
            Icons.Default.Map,
            this.contact.address.toReadableAddress().mapIntent()
        ),
    )
}

fun Breeds.toDescription(): String {
    val breadsValue = secondary?.let {
        "${this.primary}, ${this.secondary}"
    } ?: run {
        this.primary
    }
    val mixedValue = if (this.mixed) "mixed" else null
    val unknownValue = if (this.unknown) "unknown" else null

    return listOfNotNull(breadsValue, mixedValue, unknownValue).joinToString(", ")
}

fun AnimalAttributes.toDescription(): String {
    val houseTrainedValue = if (this.house_trained) "house trained" else null
    val declawedValue = if (this.declawed) "declawed" else null
    val specialNeedsValue = if (this.special_needs) "special needs" else null
    val shotsCurrentValue = if (this.shots_current) "shots current" else null
    return listOfNotNull(houseTrainedValue, declawedValue, specialNeedsValue, shotsCurrentValue).joinToString(", ")
}
fun AnimalEnvironment.toDescription(): String {
    val catsValue = if (this.cats) "cats" else null
    val childrenValue = if (this.children) "children" else null
    val dogsValue = if (this.dogs) "dogss" else null
    return listOfNotNull(catsValue, childrenValue, dogsValue).joinToString(", ")
}

fun AnimalColors.toDescription(): String {
    return listOfNotNull(this.primary, this.secondary, this.tertiary).joinToString(", ")
}

data class AnimalContact(
    val email: String? = null,
    val phone: String? = null,
    val address: AnimalContactAddress
)

data class AnimalContactAddress(
    val address1: String?,
    val address2: String?,
    val city: String,
    val state: String,
    val postcode: String,
    val country: String
)

fun AnimalContactAddress.toReadableAddress(): String {
    return "${this.address1 ?: ""} ${this.address2 ?: ""} ${this.city}, ${this.state}, ${this.postcode}, ${this.country}"
}

data class AnimalAttributes(
    val spayed_neutered: Boolean,
    val house_trained: Boolean,
    val declawed: Boolean,
    val special_needs: Boolean,
    val shots_current: Boolean
)

data class AnimalEnvironment(
    val children: Boolean,
    val dogs: Boolean,
    val cats: Boolean
)

fun mockAnimal(): Animal {
    val gson = Gson()
    val mockData = "{\"id\":50672457,\"organization_id\":\"NJ387\",\"url\":\"https://www.petfinder.com/dog/crystal-50672457/nj/palisades-park/animal-life-savers-inc-nj387/?referrer_id=e9e186a9-3fb2-4e4e-b3ad-889b2a2f0d4f\",\"type\":\"Dog\",\"species\":\"Dog\",\"breeds\":{\"primary\":\"Mastiff\",\"secondary\":null,\"mixed\":false,\"unknown\":false},\"colors\":{\"primary\":null,\"secondary\":null,\"tertiary\":null},\"age\":\"Adult\",\"gender\":\"Female\",\"size\":\"Extra Large\",\"coat\":\"Short\",\"attributes\":{\"spayed_neutered\":true,\"house_trained\":true,\"declawed\":null,\"special_needs\":false,\"shots_current\":true},\"environment\":{\"children\":null,\"dogs\":true,\"cats\":true},\"tags\":[],\"name\":\"Crystal\",\"description\":\"This is crystal. She is six years old spayed mastiff. Her owner  is sick and can&#039;t take care of her...\",\"organization_animal_id\":null,\"photos\":[{\"small\":\"https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/50672457/4/?bust=1614373972&width=100\",\"medium\":\"https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/50672457/4/?bust=1614373972&width=300\",\"large\":\"https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/50672457/4/?bust=1614373972&width=600\",\"full\":\"https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/50672457/4/?bust=1614373972\"},{\"small\":\"https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/50672457/1/?bust=1614373899&width=100\",\"medium\":\"https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/50672457/1/?bust=1614373899&width=300\",\"large\":\"https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/50672457/1/?bust=1614373899&width=600\",\"full\":\"https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/50672457/1/?bust=1614373899\"},{\"small\":\"https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/50672457/2/?bust=1614373931&width=100\",\"medium\":\"https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/50672457/2/?bust=1614373931&width=300\",\"large\":\"https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/50672457/2/?bust=1614373931&width=600\",\"full\":\"https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/50672457/2/?bust=1614373931\"},{\"small\":\"https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/50672457/3/?bust=1614373931&width=100\",\"medium\":\"https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/50672457/3/?bust=1614373931&width=300\",\"large\":\"https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/50672457/3/?bust=1614373931&width=600\",\"full\":\"https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/50672457/3/?bust=1614373931\"}],\"primary_photo_cropped\":{\"small\":\"https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/50672457/4/?bust=1614373972&width=300\",\"medium\":\"https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/50672457/4/?bust=1614373972&width=450\",\"large\":\"https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/50672457/4/?bust=1614373972&width=600\",\"full\":\"https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/50672457/4/?bust=1614373972\"},\"videos\":[],\"status\":\"adoptable\",\"status_changed_at\":\"2021-02-26T21:18:29+0000\",\"published_at\":\"2021-02-26T21:18:29+0000\",\"distance\":14.2936,\"contact\":{\"email\":\"elaine@animallifesavers.org\",\"phone\":\"(201) 335-7387\",\"address\":{\"address1\":\"P.O. Box 12\",\"address2\":null,\"city\":\"Palisades Park\",\"state\":\"NJ\",\"postcode\":\"07650\",\"country\":\"US\"}},\"_links\":{\"self\":{\"href\":\"/v2/animals/50672457\"},\"type\":{\"href\":\"/v2/types/dog\"},\"organization\":{\"href\":\"/v2/organizations/nj387\"}}}"
    return gson.fromJson(mockData, Animal::class.java)
}
