package app.kcs.komata

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import app.kcs.komata.ui.KomataApp
import app.kcs.komata.ui.theme.KomataTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KomataTheme {
                KomataApp()
            }
        }
    }
}
