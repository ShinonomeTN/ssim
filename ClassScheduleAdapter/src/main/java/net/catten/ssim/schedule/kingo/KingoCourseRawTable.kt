package net.catten.ssim.schedule.kingo

/**
 * Created by CattenLinger on 2017/2/23.
 */
class KingoCourseRawTable {
    var term: String? = null
    var unit: String? = null
    var course: String? = null
    var totalPeriod: String? = null
    var credit: String? = null
    var teacher: String? = null
    var classNum: String? = null
    var memberCount: String? = null
    var category: String? = null
    var examineType: String? = null
    var attendClass: String? = null
    var weeks: String? = null
    var timePoint: String? = null
    var address: String? = null

    override fun toString(): String {
        return "KingoCourseRawTable(term=$term, unit=$unit, course=$course, totalPeriod=$totalPeriod, credit=$credit, teacher=$teacher, classNum=$classNum, memberCount=$memberCount, category=$category, examineType=$examineType, attendClass=$attendClass, weeks=$weeks, timePoint=$timePoint, address=$address)"
    }
}
