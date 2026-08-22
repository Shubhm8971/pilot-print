package com.example.model

enum class BindingType(val label: String) {
    NONE("None"),
    STAPLE("Staple"),
    SPIRAL("Spiral")
}

enum class ColorOutput(val label: String) {
    BW("B&W"),
    COLOR("Color")
}

enum class SidesOption(val label: String) {
    SINGLE("Single-sided"),
    DOUBLE("Double-sided")
}

enum class OrderStatus(val label: String, val stepIndex: Int) {
    PAYMENT("Payment", 0),
    QUEUED("Queued", 1),
    PRINTING("Printing", 2),
    READY("Ready", 3),
    PICKED_UP("Picked Up", 4)
}

data class PrintDocument(
    val id: String,
    val name: String,
    val pageCount: Int,
    val isPdf: Boolean = true,
    val sizeLabel: String = "2.4 MB"
)

data class PrintShop(
    val id: String,
    val name: String,
    val building: String,
    val distanceMiles: Double,
    val waitingStudents: Int,
    val bwPricePerPage: Double,
    val colorPricePerPage: Double,
    val rating: Double,
    val isFast: Boolean = true,
    val imageUrl: String = ""
)

data class PrintConfig(
    val colorOutput: ColorOutput = ColorOutput.COLOR,
    val sides: SidesOption = SidesOption.SINGLE,
    val binding: BindingType = BindingType.NONE,
    val copies: Int = 1,
    val isPriorityQueue: Boolean = false,
    val useCoinsDiscount: Boolean = false
)

data class StudentProfile(
    val name: String = "Harshit Goel",
    val registrationNumber: String = "RA2511003030181",
    val email: String = "hg3311@srmist.edu.in",
    val university: String = "SRM Institute of Science and Technology",
    val coinsBalance: Int = 240,
    val avatarUrl: String = ""
)

data class PrintOrder(
    val id: String,
    val shopName: String,
    val locationInfo: String,
    val documents: List<PrintDocument>,
    val totalPages: Int,
    val config: PrintConfig,
    val status: OrderStatus,
    val queuePosition: Int,
    val estWaitMinutes: Int,
    val pickupOtp: String,
    val totalCost: Double,
    val timestamp: String,
    val buddyName: String? = null
)

data class BuddyRequest(
    val id: String,
    val requesterName: String,
    val initials: String,
    val shopName: String,
    val rewardCoins: Int,
    val pageCount: Int,
    val note: String,
    val pickupOtp: String = "4829",
    val status: String = "Open"
)

data class RewardItem(
    val id: String,
    val title: String,
    val description: String,
    val coinCost: Int,
    val iconName: String,
    val category: String
)
