package com.afrouzi.apporigin.data.model

data class StoreStats(
    val storeType: StoreType,
    val count: Int,
    val ownedCount: Int,
)

data class AuditSummary(
    val totalApps: Int = 0,
    val userApps: Int = 0,
    val systemApps: Int = 0,
    val protectedApps: Int = 0,
    val orphanedApps: Int = 0,
    val healthScore: Int = 0,
    val storeStatsList: List<StoreStats> = emptyList(),
) {
    companion object {
        fun fromApps(apps: List<AppItem>): AuditSummary {
            val total = apps.size
            val system = apps.count { it.isSystemApp }
            val user = total - system
            val protected = apps.count { !it.isSystemApp && it.hasUpdateOwner }
            val orphaned = apps.count { it.isOrphaned }

            val healthScore = if (user > 0) {
                ((protected.toDouble() / user) * 100).toInt().coerceIn(0, 100)
            } else {
                100
            }

            val storeStatsMap = apps.groupBy { it.storeType }
                .map { (storeType, list) ->
                    StoreStats(
                        storeType = storeType,
                        count = list.size,
                        ownedCount = list.count { it.hasUpdateOwner },
                    )
                }
                .sortedByDescending { it.count }

            return AuditSummary(
                totalApps = total,
                userApps = user,
                systemApps = system,
                protectedApps = protected,
                orphanedApps = orphaned,
                healthScore = healthScore,
                storeStatsList = storeStatsMap,
            )
        }
    }
}
