package features.presentation.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.language.Logger
import core.logic.FeedbackController
import core.logic.FeedbackControllerImpl
import features.data.TaskRepositoryImpl
import features.domain.PriorityModel
import features.domain.StatusModel
import features.domain.TaskModel
import features.presentation_logic.TaskListController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel(), TaskListController,
    FeedbackController by FeedbackControllerImpl() {
    private val repository = TaskRepositoryImpl.create()
    override val tasks = MutableStateFlow<List<TaskModel>>(emptyList())
    private val tag = "TaskListViewModel"
//    init {
//        viewModelScope.launch {
//            TestTasks.tasks.forEach {
//                repository.createOrThrow(it)
//            }
//
//        }
//    }
    override fun read() {
        viewModelScope.launch {
            try {
                startLoading()
                tasks.value = repository.readTasksOrThrow()
            } catch (e: Exception) {

            } finally {
                stopLoading()
            }

        }
    }

    override fun delete(id: String) {
        //Write operation, cancel if already processing
        if (proccessing()) {
            updateMessage("Already processing, try again later")
        }
        viewModelScope.launch {
            try {
                startLoading()
                repository.deleteOrThrow(id)
                read()
            } catch (e: Exception) {

            } finally {
                stopLoading()
            }

        }
    }

    override fun search(query: String?) {
        if (proccessing()) {
            updateMessage("Already processing, try again later")
            return
        }
        if (query.isNullOrEmpty()) {
            read()
            return
        }
        viewModelScope.launch {
            try {
                startLoading()
                tasks.value=repository.searchOrThrow(query)
            } catch (e: Exception) {

            } finally {
                stopLoading()
            }

        }
    }
    override fun filter(
        status: String?,
        priority: String?,
        dateRange: Pair<Long?, Long?>
    ) {
        Logger.on(tag,"FiflterData:$status, $priority, $dateRange")

        viewModelScope.launch {
            try {
                startLoading()
                tasks.value=repository.filterOrThrow(
                    status = status?.let { StatusModel.toStatusOrThrow(it) },
                    priority = priority?.let { PriorityModel.toPriorityOrThrow(it)},
                    dateRange = dateRange
                )
                Logger.on(tag,"filter: $status, $priority, $dateRange")
            } catch (e: Throwable) {
                Logger.on(tag,"filter: $e")
            } finally {
                stopLoading()
            }

        }
    }

    override fun sortByPriority() {
        viewModelScope.launch {
            try {
                startLoading()
                tasks.value=repository.prioritySortOrThrow()
            } catch (e: Throwable) {
                Logger.on(tag,"filter: $e")
            } finally {
                stopLoading()
            }
        }

    }

    override fun sortByStatus() {
        viewModelScope.launch {
            try {
                startLoading()
                tasks.value=repository.byStatusSortOrThrow()
            } catch (e: Throwable) {
                Logger.on(tag,"filter: $e")
            } finally {
                stopLoading()
            }
        }
    }

    override fun sortByDate() {
        viewModelScope.launch {
            try {
                startLoading()
                tasks.value=repository.byDateSortOrThrow()
            } catch (e: Throwable) {
                Logger.on(tag,"filter: $e")
            } finally {
                stopLoading()
            }
        }
    }
}


//Just for quick UI test
object TestTasks {

    val tasks = listOf(
        TaskModel(
            title = "Design and Implement a Fully Functional Modern Login Screen With Validation and Error Handling",
            description = "Create a modern login UI with email, password, input validation, error indicators, and smooth animations to ensure the first user impression feels professional.",
            dueDate = 1736500000000,
            priority = PriorityModel.HIGH,
            status = StatusModel.TODO,
            createdOn = 1736400000000,
            id = "t1"
        ),
        TaskModel(
            title = "Build Complete Authentication API Integration Including Token Refresh and Error Management",
            description = "Integrate the authentication API including login, token refresh logic, error handling, and proper retry mechanism ensuring reliability under poor network conditions.",
            dueDate = 1736600000000,
            priority = PriorityModel.HIGH,
            status = StatusModel.InProgress,
            createdOn = 1736400500000,
            id = "t2"
        ),
        TaskModel(
            title = "Write Comprehensive Unit Test Coverage for Repositories, ViewModels, and Data Mappers",
            description = "Add comprehensive unit tests for repositories, view models, and data mappers ensuring at least 70% coverage for core logic components in the codebase.",
            dueDate = 1736700000000,
            priority = PriorityModel.MEDIUM,
            status = StatusModel.TODO,
            createdOn = 1736401000000,
            id = "t3"
        ),
        TaskModel(
            title = "Create and Finalize the Local Database Schema With Migrations and Indexing Strategy",
            description = "Define the full local database schema including tables, indexes, relations, migrations, and fallback strategy using Room to maintain app stability.",
            dueDate = 1736800000000,
            priority = PriorityModel.HIGH,
            status = StatusModel.DONE,
            createdOn = 1736401500000,
            id = "t4"
        ),
        TaskModel(
            title = "Setup CI/CD Pipeline With Build Checks, Style Validation, Unit Tests and Auto Release",
            description = "Configure CI/CD pipelines using GitHub Actions including build checks, code style validation, unit test automation, and automated release artifacts.",
            dueDate = 1736900000000,
            priority = PriorityModel.MEDIUM,
            status = StatusModel.TODO,
            createdOn = 1736402000000,
            id = "t5"
        ),
        TaskModel(
            title = "Refactor Entire Networking Layer and Migrate From Retrofit to Ktor With Custom Handlers",
            description = "Migrate all networking logic from Retrofit to Ktor including custom interceptors, JSON parser configuration, timeout settings, and unified error mapping.",
            dueDate = 1737000000000,
            priority = PriorityModel.LOW,
            status = StatusModel.InProgress,
            createdOn = 1736402500000,
            id = "t6"
        ),
        TaskModel(
            title = "Optimize and Compress All App Images to Reduce APK Size Without Losing Quality",
            description = "Compress all image assets to reduce APK size while maintaining visual clarity, including vector optimization and resizing large PNG files.",
            dueDate = 1737100000000,
            priority = PriorityModel.LOW,
            status = StatusModel.TODO,
            createdOn = 1736403000000,
            id = "t7"
        ),
        TaskModel(
            title = "Implement Full Push Notification System Using FCM With Token and Topic Support",
            description = "Implement FCM push notifications including token handling, topic subscription, message parsing, and user-specific notification channel setup.",
            dueDate = 1737200000000,
            priority = PriorityModel.HIGH,
            status = StatusModel.InProgress,
            createdOn = 1736403500000,
            id = "t8"
        ),
        TaskModel(
            title = "Add System-Wide Dark Mode Support With Animated Theme Switching",
            description = "Add full dark mode support including theme switching, resource overlays, color palette adjustments, and smooth transition animations.",
            dueDate = 1737300000000,
            priority = PriorityModel.MEDIUM,
            status = StatusModel.DONE,
            createdOn = 1736404000000,
            id = "t9"
        ),
        TaskModel(
            title = "Design Multi-Step Onboarding Screens With Illustrations and Smooth Transitions",
            description = "Create a 3-step onboarding flow with illustrations, transitions, user education points, and state management for showing the screens only once.",
            dueDate = 1737400000000,
            priority = PriorityModel.MEDIUM,
            status = StatusModel.TODO,
            createdOn = 1736404500000,
            id = "t10"
        ),
        TaskModel(
            title = "Setup Firebase Crashlytics With Detailed Logging and Performance Tracking",
            description = "Integrate Firebase Crashlytics with detailed error logging, non-fatal reports, performance traces, and custom keys for advanced debugging.",
            dueDate = 1737500000000,
            priority = PriorityModel.HIGH,
            status = StatusModel.DONE,
            createdOn = 1736405000000,
            id = "t11"
        ),
        TaskModel(
            title = "Improve App Animations by Fine-Tuning Home Screen and List Interactions",
            description = "Add fluid animations for home screen transitions, button presses, and list item interactions to enhance overall UI smoothness.",
            dueDate = 1737600000000,
            priority = PriorityModel.LOW,
            status = StatusModel.InProgress,
            createdOn = 1736405500000,
            id = "t12"
        ),
        TaskModel(
            title = "Add Real-Time Search Functionality With Debounce and Optimized List Updates",
            description = "Implement text-based search for filtering tasks by title and description using a real-time query with debounce and optimized list updates.",
            dueDate = 1737700000000,
            priority = PriorityModel.MEDIUM,
            status = StatusModel.TODO,
            createdOn = 1736406000000,
            id = "t13"
        ),
        TaskModel(
            title = "Implement Sorting and Filtering Capabilities With Saved User Preferences",
            description = "Add sorting and filtering options for tasks by priority, status, and due date with persistent user preferences and animated list changes.",
            dueDate = 1737800000000,
            priority = PriorityModel.MEDIUM,
            status = StatusModel.TODO,
            createdOn = 1736406500000,
            id = "t14"
        ),
        TaskModel(
            title = "Develop Complete Offline Mode With Caching, Sync Logic, and Conflict Handling",
            description = "Enable full offline support using cached responses, background sync, stale data strategy, and conflict-handling logic for local updates.",
            dueDate = 1737900000000,
            priority = PriorityModel.HIGH,
            status = StatusModel.InProgress,
            createdOn = 1736407000000,
            id = "t15"
        ),
        TaskModel(
            title = "Build User Profile Page With Avatar Upload and Editable Account Information",
            description = "Implement user profile page including avatar upload, account details editing, and responsive layout adjustments for tablets.",
            dueDate = 1738000000000,
            priority = PriorityModel.LOW,
            status = StatusModel.TODO,
            createdOn = 1736407500000,
            id = "t16"
        ),
        TaskModel(
            title = "Implement Full Settings Screen With Notification Controls and Theme Options",
            description = "Add settings screen with notification toggles, theme selection, privacy options, and session logout capability.",
            dueDate = 1738100000000,
            priority = PriorityModel.LOW,
            status = StatusModel.DONE,
            createdOn = 1736408000000,
            id = "t17"
        ),
        TaskModel(
            title = "Enhance Overall App UX With Better Spacing, Typography, and Touch Feedback",
            description = "Enhance overall UX by refining spacing, padding, typography, touch areas, and interactive feedback throughout the app.",
            dueDate = 1738200000000,
            priority = PriorityModel.MEDIUM,
            status = StatusModel.InProgress,
            createdOn = 1736408500000,
            id = "t18"
        ),
        TaskModel(
            title = "Fix All Known QA-Reported Bugs Including Layout Issues and Formatting Problems",
            description = "Resolve three known bugs reported by QA including layout overlap issues, incorrect date formatting, and missing error messages.",
            dueDate = 1738300000000,
            priority = PriorityModel.HIGH,
            status = StatusModel.TODO,
            createdOn = 1736409000000,
            id = "t19"
        ),
        TaskModel(
            title = "Prepare Final Release Build With Versioning Updates, Signing, and Play Store Bundle",
            description = "Prepare the final release build with versioning updates, ProGuard rules, signing configuration, and Play Store bundle generation.",
            dueDate = 1738400000000,
            priority = PriorityModel.HIGH,
            status = StatusModel.DONE,
            createdOn = 1736409500000,
            id = "t20"
        )
    )
}
