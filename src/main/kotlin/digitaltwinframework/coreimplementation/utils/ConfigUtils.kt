package digitaltwinframework.coreimplementation.utils

/**
 * Created by Matteo Gabellini on 24/01/2020.
 */
object ConfigUtils {

    fun projectPathUri(): String {
        return "file://${System.getProperty("user.dir")}"
    }

    fun resourceFolderPath(): String {
        return projectPathUri() + "/res/"
    }

    fun createUri(relativePath: String): String {
        return cleanSpace(resourceFolderPath() + relativePath)
    }

    fun cleanSpace(path: String): String {
        return path.replace(
            " ",
            "%20"
        )
    }
}