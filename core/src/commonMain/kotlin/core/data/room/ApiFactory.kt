@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING","KotlinNoActualForExpect")

package core.data.room

import core.data.api.TaskApi

expect object ApiFactory {
    fun init(context: Any)
    fun createTaskApiOrThrow(): TaskApi
}