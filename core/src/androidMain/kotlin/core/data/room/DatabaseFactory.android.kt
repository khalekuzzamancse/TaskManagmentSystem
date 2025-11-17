package core.data.room
import android.content.Context
import core.data.api.TaskApi
import core.data.local.TaskLocalDataSrc

@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
actual object ApiFactory {
    private var database: AppDatabase? = null
    actual fun init(context: Any) {
       if(database == null){
           database = getRoomDatabase(getDatabaseBuilder(context as Context))
       }
        //Logger.off("DatabaseFactory::init()","database:$database")
    }
    actual fun createTaskApiOrThrow(): TaskApi {
        return TaskLocalDataSrc(database!!.getDao())
    }
}