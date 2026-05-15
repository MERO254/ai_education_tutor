package com.example.ai_education_tutor.components

import com.example.ai_education_tutor.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ai_education_tutor.ui.theme.ActiveBlue
import com.example.ai_education_tutor.ui.theme.ActiveIndicator
import com.example.ai_education_tutor.ui.theme.InactiveGray
import com.example.ai_education_tutor.ui.theme.NavWhite
import kotlinx.coroutines.selects.select
import java.nio.file.WatchEvent


@Composable
fun BottomNavBar(navController: NavController){

    val items = listOf(
        //bottom nav items
        BottomNavItem.Home,
        BottomNavItem.Course,
        BottomNavItem.AI_tutor,
        BottomNavItem.Profile
    )

    val backStackEntry = navController.currentBackStackEntryAsState()

    val currentRoute = backStackEntry.value?.destination?.route

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 10.dp,
                end = 10.dp,
                bottom = 10.dp
            ),

        elevation = 12.dp,

        backgroundColor = NavWhite,

        shape = RoundedCornerShape(20.dp)
    ) {

        BottomNavigation(
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            modifier = Modifier.height(78.dp)
        ){

            items.forEach { item ->

                val selected = currentRoute == item.route

                BottomNavigationItem(
                    selected = selected,



                    onClick = {
                        navController.navigate(item.route){
                            popUpTo(
                                navController.graph.startDestinationId
                            ){
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    },

                    selectedContentColor = ActiveBlue,
                    unselectedContentColor = InactiveGray,

                    icon = {

                        Box(
                           modifier = Modifier
                               .size(42.dp)
                               .clip(RoundedCornerShape(10.dp))
                               .background(
                                   if(selected){
                                       ActiveIndicator
                                   }else{
                                       Color.Transparent
                                   }
                               ),

                           contentAlignment = Alignment.Center
                        ){

                            Icon(
                                painter = when(item.route){
                                    "home" -> painterResource(
                                        id = R.drawable.ic_home
                                    )

                                    "courses" -> painterResource(
                                        id = R.drawable.ic_course
                                    )

                                    "AI_tutor" -> painterResource(
                                        id = R.drawable.ic_ai
                                    )

                                    "profile" -> painterResource(
                                        id = R.drawable.ic_profile
                                    )

                                    else -> painterResource(
                                        id = R.drawable.ic_home
                                    )


                                },
                                contentDescription = item.title,

                                tint = if (selected){
                                    ActiveBlue
                                }else{
                                    InactiveGray
                                },

                                modifier = Modifier.size(22.dp)
                            )



                        }

                    },

                    label = {
                        Text(text = item.title)
                    }
                )
            }

        }

    }

}