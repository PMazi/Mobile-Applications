package com.example.koktajle

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.koktajle.ui.theme.KoktajleTheme
import com.example.koktajle.ui.theme.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.TextUnit
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults


import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext


class Drink(val name: String, val rating: Int, val imageResId: Int, val ingredient: String, val description: String){}


class MainActivity : ComponentActivity() {

    val AlcoholDrinksInfo = listOf(
        Drink(
            "Mojito", 5, R.drawable.mojito,
            "50 ml białego rumu, 1/2 limonki, 2 łyżeczki cukru, kilka listków mięty, woda gazowana, kruszony lód",
            "Limonkę pokrój na ćwiartki i wrzuć do szklanki. Dodaj cukier i rozgnieć muddlerem, aby wydobyć sok. Dodaj listki mięty i delikatnie je ugnieć. Następnie wsyp kruszony lód, wlej rum i dopełnij wodą gazowaną. Wymieszaj i udekoruj miętą."
        ),
        Drink(
            "Margarita", 4, R.drawable.margarita,
            "50 ml tequili, 20 ml likieru pomarańczowego (Triple Sec), 20 ml świeżo wyciśniętego soku z limonki, sól, lód",
            "Brzeg kieliszka zanurz w soku z limonki, a następnie w soli, aby uzyskać charakterystyczną obręcz. W shakerze wymieszaj tequilę, likier pomarańczowy i sok z limonki z lodem. Wstrząśnij energicznie i przelej do kieliszka przez sitko."
        ),
        Drink(
            "Piña Colada", 3, R.drawable.pina_colada,
            "50 ml białego rumu, 100 ml soku ananasowego, 50 ml mleka kokosowego, kruszony lód, plaster ananasa",
            "Wszystkie składniki wlej do blendera i zmiksuj na gładką, kremową konsystencję. Przelej do wysokiej szklanki, udekoruj plastrem ananasa i podawaj ze słomką."
        ),
        Drink(
            "Cosmopolitan", 5, R.drawable.cosmopolitan,
            "40 ml wódki cytrynowej, 20 ml likieru pomarańczowego (Triple Sec), 30 ml soku żurawinowego, 10 ml soku z limonki, lód",
            "Wszystkie składniki umieść w shakerze z lodem i energicznie wstrząśnij. Przelej do schłodzonego kieliszka koktajlowego i udekoruj skórką z pomarańczy."
        ),
        Drink(
            "Caipirinha", 1, R.drawable.caipirinha,
            "50 ml cachaçy, 1 limonka, 2 łyżeczki cukru trzcinowego, kruszony lód",
            "Limonkę pokrój na ćwiartki, wrzuć do szklanki i zasyp cukrem. Użyj muddlera, aby wydobyć sok. Następnie dodaj kruszony lód i wlej cachaçę. Wymieszaj i podawaj natychmiast."
        ),
        Drink(
            "Daiquiri", 3, R.drawable.daiquiri,
            "50 ml białego rumu, 25 ml świeżego soku z limonki, 15 ml syropu cukrowego, lód",
            "Wszystkie składniki wlej do shakera wypełnionego lodem. Wstrząśnij energicznie i przelej do schłodzonego kieliszka koktajlowego."
        ),
        Drink(
            "Old Fashioned", 4, R.drawable.old_fashioned,
            "50 ml bourbonu, 1 kostka cukru, kilka kropli Angostura Bitters, skórka pomarańczy, lód",
            "Do niskiej szklanki wrzuć kostkę cukru i dodaj kilka kropli Angostury. Rozgnieć, aby cukier się rozpuścił. Dodaj kilka kostek lodu, wlej bourbon i delikatnie wymieszaj. Udekoruj skórką pomarańczy."
        ),
        Drink(
            "Negroni", 5, R.drawable.negroni,
            "30 ml ginu, 30 ml Campari, 30 ml czerwonego wermutu, lód, skórka pomarańczy",
            "Do niskiej szklanki wrzuć lód, a następnie wlej gin, Campari i wermut. Wymieszaj delikatnie i udekoruj skórką pomarańczy."
        ),
        Drink(
            "Whiskey Sour", 2, R.drawable.whiskey_sour,
            "50 ml bourbonu, 25 ml soku z cytryny, 15 ml syropu cukrowego, białko jajka, lód",
            "Wszystkie składniki umieść w shakerze bez lodu i energicznie wstrząśnij, aby spienić białko. Następnie dodaj lód i ponownie wstrząśnij. Przelej do szklanki przez sitko i udekoruj wisienką koktajlową."
        ),
        Drink(
            "Mai Tai", 4, R.drawable.mai_tai,
            "40 ml jasnego rumu, 20 ml ciemnego rumu, 15 ml likieru pomarańczowego, 10 ml syropu migdałowego, 10 ml soku z limonki",
            "Wstrząśnij jasny rum, likier pomarańczowy, syrop migdałowy i sok z limonki w shakerze z lodem. Przelej do szklanki, a następnie powoli dolej ciemny rum, aby unosił się na wierzchu. Udekoruj miętą i plasterkiem limonki."
        ),
        Drink(
            "Bloody Mary", 3, R.drawable.bloody_mary,
            "50 ml wódki, 150 ml soku pomidorowego, 15 ml soku z cytryny, kilka kropli Tabasco, kilka kropli sosu Worcestershire, sól, pieprz, seler naciowy",
            "Do wysokiej szklanki wypełnionej lodem wlej wódkę i sok pomidorowy. Dodaj sok z cytryny, przyprawy oraz Tabasco i Worcestershire. Wymieszaj i udekoruj selerem naciowym."
        ),
        Drink(
            "Gin Fizz", 1, R.drawable.gin_fizz,
            "50 ml ginu, 25 ml soku z cytryny, 15 ml syropu cukrowego, woda gazowana, lód",
            "Wszystkie składniki (oprócz wody gazowanej) wstrząśnij w shakerze z lodem. Przelej do szklanki i dopełnij wodą gazowaną."
        ),
        Drink(
            "Tequila Sunrise", 4, R.drawable.tequila_sunrise,
            "50 ml tequili, 100 ml soku pomarańczowego, 15 ml grenadyny, lód",
            "Do wysokiej szklanki wlej tequilę i sok pomarańczowy z lodem. Powoli dolej grenadynę, która opadnie na dno, tworząc efekt 'wschodu słońca'."
        ),
        Drink(
            "Cuba Libre", 5, R.drawable.cuba_libre,
            "50 ml rumu, 100 ml coli, 1/2 limonki, lód",
            "Do szklanki z lodem wlej rum, dopełnij colą i wyciśnij sok z połowy limonki. Udekoruj plasterkiem limonki."
        ),
        Drink(
            "Sex on the Beach", 5, R.drawable.sex_on_the_beach,
            "40 ml wódki, 20 ml likieru brzoskwiniowego, 50 ml soku pomarańczowego, 50 ml soku żurawinowego, lód",
            "Wszystkie składniki wstrząśnij w shakerze z lodem i przelej do szklanki wypełnionej lodem."
        )
    )

    val NoAlcoholDrinksInfo = listOf(
        Drink(
            "Virgin Mojito", 5, R.drawable.mojito,
            "1/2 limonki, 2 łyżeczki cukru, kilka listków mięty, woda gazowana, kruszony lód",
            "Pokrój limonkę na ćwiartki i wrzuć do szklanki. Dodaj cukier i rozgnieć muddlerem. Dodaj listki mięty i delikatnie je ugnieć. Wsyp kruszony lód i dopełnij wodą gazowaną. Wymieszaj i udekoruj miętą."
        ),
        Drink(
            "Shirley Temple", 4, TODO,
            "100 ml lemoniady (Sprite/7up), 20 ml grenadyny, wisienka koktajlowa, lód",
            "Do szklanki z lodem wlej lemoniadę, a następnie dodaj grenadynę. Udekoruj wisienką koktajlową."
        ),
        Drink(
            "Virgin Piña Colada", 3, TODO,
            "100 ml soku ananasowego, 50 ml mleka kokosowego, kruszony lód, plaster ananasa",
            "Zmiksuj wszystkie składniki w blenderze na kremową konsystencję. Przelej do wysokiej szklanki i udekoruj plastrem ananasa."
        ),
        Drink(
            "Nojito Truskawkowe", 4, TODO,
            "1/2 limonki, kilka listków mięty, 2 truskawki, 2 łyżeczki cukru, woda gazowana, kruszony lód",
            "Limonkę, truskawki i cukier rozgnieć w szklance. Dodaj miętę i lekko ugnieć. Wsyp lód, dopełnij wodą gazowaną i udekoruj miętą."
        ),
        Drink(
            "Lemoniada Arbuzowa", 5, TODO,
            "200 g arbuza, 100 ml wody, sok z 1/2 cytryny, 1 łyżeczka miodu, lód",
            "Zblenduj arbuza z wodą, cytryną i miodem. Przelej przez sitko, wlej do szklanki z lodem i udekoruj listkiem mięty."
        ),
        Drink(
            "Smoothie Mango-Banan", 3, TODO,
            "1 mango, 1 banan, 100 ml soku pomarańczowego, kilka kostek lodu",
            "Zblenduj wszystkie składniki do uzyskania gładkiej konsystencji. Podawaj w wysokiej szklance ze słomką."
        ),
        Drink(
            "Blue Lagoon Virgin", 4, TODO,
            "100 ml lemoniady, 20 ml syropu blue curacao bezalkoholowego, lód, plaster cytryny",
            "Do szklanki z lodem wlej lemoniadę, a następnie dodaj syrop blue curacao. Wymieszaj i udekoruj plasterkiem cytryny."
        )
    )

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var appBarHeight by remember { mutableStateOf(25.dp) }
            val navController = rememberNavController()
            KoktajleTheme {
                Scaffold(topBar = {
                    TopAppBar( modifier = Modifier.height(appBarHeight),
                        colors = topAppBarColors(
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = {

                        }
                    )
                },
                modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val finalPadding = appBarHeight
                    Navig(navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        AlcoholDrinksInfo,
                        NoAlcoholDrinksInfo,
                        onNavChange = { screen ->
                            appBarHeight = when (screen) {
                                "DrinksList" -> 25.dp
                                "showDrinkInfo" -> 25.dp
                                else -> 0.dp
                            }
                        }
                    )
                }
            }
        }
    }
}



@Composable
fun Navig(navController: NavHostController,
          modifier: Modifier = Modifier,
          AlcoholDrinksInfo: List<Drink>,
          NoAlcoholDrinksInfo: List<Drink>,
          onNavChange: (String) -> Unit
) {
    NavHost(navController = navController, startDestination = "DrinksList"){
        composable(route="DrinksList"){
            onNavChange("DrinksList")
            DrinksList(
                onDrinksListClick = { drink -> navController.navigate(route = "showDrinkInfo/${drink.name}")},
                AlcoholDrinksInfo,
                NoAlcoholDrinksInfo
            )
        }
        composable(route = "showDrinkInfo/{drinkName}") { backStackEntry ->
            val drinkName = backStackEntry.arguments?.getString("drinkName")
            val drink = AlcoholDrinksInfo.firstOrNull { it.name == drinkName }
            if (drink != null) {
                onNavChange("showDrinkInfo")
                showDrinkInfo(drink,
                    backButton = {navController.navigate(route = "DrinksList")}
                )
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinksList(onDrinksListClick: (Drink) -> Unit = {},
               AlcoholDrinksInfo: List<Drink>,
               NoAlcoholDrinksInfo: List<Drink>,
               modifier: Modifier = Modifier
) {
    var showAlcoholDrinks by rememberSaveable {mutableStateOf(true)}
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Button(
                            onClick = { showAlcoholDrinks = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent, // Brak tła
                                contentColor = MaterialTheme.colorScheme.onBackground // Domyślny kolor czcionki
                            )
                        ) {
                            if (showAlcoholDrinks) {
                                textUnderline("Alkoholowe", underlineColor = LBlue, 16.sp, FontWeight.Medium)
                            } else {
                                Text(text = "Alkoholowe", fontSize = 16.sp)
                            }
                        }
                        Button(
                            onClick = { showAlcoholDrinks = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent, // Brak tła
                                contentColor = MaterialTheme.colorScheme.onBackground // Domyślny kolor czcionki
                            )
                        ) {
                            if (!showAlcoholDrinks) {
                                textUnderline("Bezalkoholowe", underlineColor = LBlue, 16.sp, FontWeight.Medium)
                            } else {
                                Text(text = "Bezalkoholowe", fontSize = 16.sp)
                            }
                        }
                    }
                },
                modifier = Modifier.height(90.dp)
            )
        }
    ) { paddingValues ->
        LazyColumn( modifier = Modifier
            .fillMaxHeight() // Wypełnia dostępne miejsce
            .fillMaxWidth()
            .padding(paddingValues)
            .padding(start = 20.dp, end = 20.dp)
            .clip(RoundedCornerShape(16.dp)),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            val DrinksToShow = if(showAlcoholDrinks) AlcoholDrinksInfo else NoAlcoholDrinksInfo
            items(DrinksToShow) { drink ->
                Button(onClick = {onDrinksListClick(drink)},
                    colors = ButtonDefaults.buttonColors(containerColor = LBlue),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ){
                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = drink.name, color = Color.Black, fontSize = 18.sp, modifier = Modifier.padding(0.dp))
                            Image(
                                painter = painterResource(id = drink.imageResId),
                                contentDescription = "Drink Icon",
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .size(90.dp)
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically){
                            for(i in 1..5){
                                Icon(
                                    imageVector = if (i <= drink.rating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                    contentDescription = "Rating",
                                    tint = Pink
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun TimerUI(
    time: Long,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit

) {
    val configuration = LocalConfiguration.current

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
            .fillMaxHeight()
    ) {
        when(configuration.orientation) {
            //Orientacja pionowa
            Configuration.ORIENTATION_PORTRAIT -> {
                Text(text = String.format("%.2f", time / 1000.0), fontSize = 40.sp)
                Row(horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Button(onClick = onStart,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LBlue
                        )
                    ) {
                        Image(
                            painter = painterResource(R.drawable.play_icon2),
                            contentDescription = "Drink Icon",
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .size(30.dp)
                        )
                    }
                    Button(onClick = onStop,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Pink
                        )
                    ) {
                        Image(
                            painter = painterResource(R.drawable.pause_icon),
                            contentDescription = "Drink Icon",
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .size(30.dp)
                        )
                    }
                    Button(onClick = onReset,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Gray
                        )
                    ) {
                        Image(
                            painter = painterResource(R.drawable.reset_icon),
                            contentDescription = "Drink Icon",
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .size(30.dp)
                        )
                    }
                }
            }
            //Orientacja pozioma
            Configuration.ORIENTATION_LANDSCAPE -> {
                Row(horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(text = String.format("%.2f", time / 1000.0), fontSize = 40.sp)
                    Button(onClick = onStart,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LBlue
                        )
                    ) {
                        Image(
                            painter = painterResource(R.drawable.play_icon2),
                            contentDescription = "Drink Icon",
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .size(30.dp)
                        )
                    }
                    Button(onClick = onStop,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Pink
                        )
                    ) {
                        Image(
                            painter = painterResource(R.drawable.pause_icon),
                            contentDescription = "Drink Icon",
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .size(30.dp)
                        )
                    }
                    Button(onClick = onReset,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Gray
                        )
                    ) {
                        Image(
                            painter = painterResource(R.drawable.reset_icon),
                            contentDescription = "Drink Icon",
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .size(30.dp)
                        )
                    }
                }
            }
        }
    }
}


class TimerViewModel : ViewModel() {

    private val _time = mutableStateOf(0L)
    val time: State<Long> = _time

    private var startTime = 0L
    private var timerJob: Job? = null

    fun startTimer() {
        if (timerJob?.isActive == true) return

        startTime = System.currentTimeMillis() - _time.value

        timerJob = viewModelScope.launch {
            while (true) {
                _time.value = System.currentTimeMillis() - startTime
                delay(10L)
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
    }

    fun resetTimer() {
        timerJob?.cancel()
        _time.value = 0L
    }
}

@Composable
fun SMS_button(drink: Drink){

    val context = LocalContext.current
    FloatingActionButton(
        onClick = {
            val smsText =
                "Ingredients for ${drink.name} drink: ${drink.ingredient}"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("smsto:")
                putExtra("sms_body", smsText)
            }
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Nie udało się otworzyć wiadomości",
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        containerColor = Color.Transparent, // kolor tła
        elevation = FloatingActionButtonDefaults.elevation(0.dp),  // brak cienia
        modifier = Modifier.width(70.dp).height(35.dp) // zmiana rozmiaru
    ) {
//            Image(painter = painterResource(id = R.drawable.send1),
//                contentDescription = "send message"
//            )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Message,
            contentDescription = "Powrót"
        )
    }
}


@Composable
fun showDrinkInfo(
    drink: Drink,
    modifier: Modifier = Modifier,
    backButton: () -> Unit = {},
    viewModel: TimerViewModel = viewModel()
) {
    val time by viewModel.time
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(screenHeight * 0.17f),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                TimerUI(
                    time = time,
                    onStart = { viewModel.startTimer() },
                    onStop = { viewModel.stopTimer() },
                    onReset = { viewModel.resetTimer() }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxHeight()
                .padding(paddingValues)
                .padding(start = 35.dp, end = 35.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row {
                        Text(
                            drink.name,
                            color = LBlue,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row {
                        for (i in 1..5) {
                            Icon(
                                imageVector = if (i <= drink.rating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                contentDescription = "Rating",
                                tint = Pink,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Image(
                        painter = painterResource(id = drink.imageResId),
                        contentDescription = "Drink Icon",
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .size(110.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                textUnderline("Składniki", LBlue, 22.sp, FontWeight.Bold)
                Spacer(modifier = Modifier.width(screenWidth * 0.3f))
                SMS_button(drink)
                IconButton(onClick = { backButton() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Powrót"
                    )
                }
            }
            Text(
                text = drink.ingredient,
                fontSize = 18.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .padding(5.dp)
            )
            textUnderline("Przygotowanie", LBlue, 22.sp, FontWeight.Bold)
            Text(
                text = drink.description,
                fontSize = 18.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .padding(5.dp)
            )
        }
    }
}




// podkreślanie tekstu
@Composable
fun textUnderline(text: String, underlineColor: Color, fontSize: TextUnit, fontWeight: FontWeight = FontWeight.Medium){
    Text(
        text = text,
        fontWeight = fontWeight,
        fontSize = fontSize,
        modifier = Modifier
            .padding(0.dp)
            .drawBehind {
                val strokeWidth = 5f // Grubość podkreślenia
                drawLine(
                    color = underlineColor, // Kolor podkreślenia
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            }
    )
}
