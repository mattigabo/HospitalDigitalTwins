package digitaltwinframework

import hospitaldigitaltwins.App
import org.junit.Test
import kotlin.test.assertNotNull


class AppTest {
    @Test
    fun testAppHasAGreeting() {
        val classUnderTest = App()
        assertNotNull(classUnderTest.greeting, "app should have a greeting")
    }
}
