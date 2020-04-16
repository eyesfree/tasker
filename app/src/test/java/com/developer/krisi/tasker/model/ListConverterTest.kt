import com.developer.krisi.tasker.model.ListConverter
import junit.framework.Assert.assertEquals
import org.junit.Test

class ListConverterTest {

    @Test
    fun convertListToString() {
        // Create an active task
        val tasks = mutableListOf<String>(
                "fun", "tests", "java", "kotlin"
        )
        // Call your function
        val listString = ListConverter.fromArrayList(tasks);
        // Check the result
        assertEquals("[\"fun\",\"tests\",\"java\",\"kotlin\"]", listString)
    }

    @Test
    fun convertStringToList() {
        // Create an active task
        val expectedList = mutableListOf<String>(
                "fun", "tests", "java", "kotlin"
        )

        val tasksString = "[\"fun\",\"tests\",\"java\",\"kotlin\"]"
        // Call your function
        val actualList = ListConverter.fromString(tasksString);
        // Check the result
        assertEquals(expectedList, actualList)
    }
}