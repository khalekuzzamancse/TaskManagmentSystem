@file:Suppress("SpellCheckingInspection")

package features.presentation.misc

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.ui.ScreenStrategy
import core.ui.SpacerVertical
import features.R


@Preview
@Composable
private fun AboutPreview() {
    AboutUsScreen(
        fab = {},
        bottomBar = {},
        navRail = {}
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(
    fab: @Composable () -> Unit ,
    bottomBar: @Composable () -> Unit,
    navRail: @Composable () -> Unit,
) {
    ScreenStrategy(
        fab = fab,
        bottomBar = bottomBar,
        navRail = navRail,
    ) {modifier ->
        Column(
            modifier = modifier
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // "Supervised by" Heading
            SpacerVertical(32)
            SectionHeading()
            Spacer(modifier = Modifier.height(8.dp))
            // Supervisor Section
            SupervisorSection()
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))
            // Developer Section
            DeveloperSection()
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            // Copyright Notice
            CopyrightNotice()
        }
    }
}

@Composable
private fun SectionHeading() {
    Text(
        text = "Supervised by",
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary
    )
}


@Composable
private fun SupervisorSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
//        Image(
//            res = R.drawable.super_vicer,
//            modifier = Modifier
//                .size(150.dp)
//                .clip(CircleShape)
//                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
//        )

        Text(
            text = "E-Tracker Solution",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        SpacerVertical(4)
        Text(
            text = "House-537, Road-11, Baridhara DOHS",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.secondary
        )
        SpacerVertical(8)


    }
}

@Composable
private fun DeveloperSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = "App Developed by",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Image(
            res = R.drawable.developer,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Md. Khalekuzzman",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        SpacerVertical(2)
        DeptAndUniversity()

    }
}

@Composable
fun DeptAndUniversity(modifier: Modifier = Modifier) {
    Text(
        text = "B.Sc. (Engg.) in CSE from Jashore University of Science and Technology (JUST)",
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onBackground
    )

}


@Composable
fun CopyrightNotice() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 16.dp)
    ) {
        // Display the university logo
//        Image(
//            res = R.drawable.just_logo,
//            modifier = Modifier
//                .size(80.dp)
//                .clip(CircleShape)
//        )

        Spacer(modifier = Modifier.height(8.dp))

        // Copyright and University Attribution Text
        Text(
            text = "© 2025, E-Tracker Solution: House-537, Road-11, Baridhara DOHS",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Task Managment System App",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}


@Composable
private fun Image(modifier: Modifier = Modifier, res: Int) {
    Image(
        modifier = modifier,
        painter = painterResource(res),
        contentDescription = null
    )
}
