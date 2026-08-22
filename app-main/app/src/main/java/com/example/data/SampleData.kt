package com.example.data

import com.example.model.BindingType
import com.example.model.BuddyRequest
import com.example.model.ColorOutput
import com.example.model.OrderStatus
import com.example.model.PrintConfig
import com.example.model.PrintDocument
import com.example.model.PrintOrder
import com.example.model.PrintShop
import com.example.model.RewardItem
import com.example.model.SidesOption

object SampleData {

    val defaultShops = listOf(
        PrintShop(
            id = "shop-1",
            name = "Campus Print Hub",
            building = "Library Bldg",
            distanceMiles = 0.2,
            waitingStudents = 7,
            bwPricePerPage = 0.10,
            colorPricePerPage = 0.25,
            rating = 4.8,
            isFast = true,
            imageUrl = "https://images.unsplash.com/photo-1568667256549-094345857637?auto=format&fit=crop&w=600&q=80"
        ),
        PrintShop(
            id = "shop-2",
            name = "Quick Xerox",
            building = "Main St. Student Plaza",
            distanceMiles = 0.5,
            waitingStudents = 2,
            bwPricePerPage = 0.08,
            colorPricePerPage = 0.30,
            rating = 4.5,
            isFast = true,
            imageUrl = "https://images.unsplash.com/photo-1541829070764-84a7d30dd3f3?auto=format&fit=crop&w=600&q=80"
        ),
        PrintShop(
            id = "shop-3",
            name = "Student Copy Center",
            building = "West Campus Union",
            distanceMiles = 0.8,
            waitingStudents = 4,
            bwPricePerPage = 0.08,
            colorPricePerPage = 0.25,
            rating = 4.2,
            isFast = false,
            imageUrl = "https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?auto=format&fit=crop&w=600&q=80"
        ),
        PrintShop(
            id = "shop-4",
            name = "Engineering Bldg 4 Print Lab",
            building = "Tech Park Block C",
            distanceMiles = 0.5,
            waitingStudents = 2,
            bwPricePerPage = 0.08,
            colorPricePerPage = 0.30,
            rating = 4.7,
            isFast = true,
            imageUrl = "https://images.unsplash.com/photo-1497633762265-9d179a990aa6?auto=format&fit=crop&w=600&q=80"
        )
    )

    val defaultDocuments = listOf(
        PrintDocument(
            id = "doc-1",
            name = "Assignment.pdf",
            pageCount = 24,
            isPdf = true,
            sizeLabel = "3.2 MB"
        ),
        PrintDocument(
            id = "doc-2",
            name = "Project.pdf",
            pageCount = 48,
            isPdf = true,
            sizeLabel = "6.8 MB"
        )
    )

    val initialActiveOrder = PrintOrder(
        id = "PP-2026-1042",
        shopName = "Campus Print Hub",
        locationInfo = "Library Bldg Ground Floor",
        documents = listOf(
            PrintDocument("doc-active-1", "Final_Thesis_v2.pdf", 42, true, "5.1 MB"),
            PrintDocument("doc-active-2", "Appendix_Charts.pdf", 30, true, "2.9 MB")
        ),
        totalPages = 72,
        config = PrintConfig(
            colorOutput = ColorOutput.COLOR,
            sides = SidesOption.DOUBLE,
            binding = BindingType.NONE,
            copies = 1,
            isPriorityQueue = false,
            useCoinsDiscount = false
        ),
        status = OrderStatus.PRINTING,
        queuePosition = 2,
        estWaitMinutes = 6,
        pickupOtp = "482913",
        totalCost = 144.0,
        timestamp = "Today, 14:32 PM"
    )

    val initialBuddyRequests = listOf(
        BuddyRequest(
            id = "req-1",
            requesterName = "John D.",
            initials = "JD",
            shopName = "Campus Print Hub",
            rewardCoins = 25,
            pageCount = 12,
            note = "\"Need my bio lab report by 3 PM. It's paid for, just needs pickup!\"",
            pickupOtp = "4829",
            status = "Open"
        ),
        BuddyRequest(
            id = "req-2",
            requesterName = "Alice S.",
            initials = "AS",
            shopName = "Library East",
            rewardCoins = 15,
            pageCount = 4,
            note = "\"Quick poster print for a presentation. I'm stuck in class.\"",
            pickupOtp = "8492",
            status = "Open"
        ),
        BuddyRequest(
            id = "req-3",
            requesterName = "Rahul K.",
            initials = "RK",
            shopName = "Engineering Bldg 4",
            rewardCoins = 30,
            pageCount = 20,
            note = "\"Circuit schematic blueprint sheets ready at desk 2. Room 402 drop-off.\"",
            pickupOtp = "9134",
            status = "Open"
        )
    )

    val sampleRewards = listOf(
        RewardItem("rew-1", "2 Free Color Pages", "Save coins on color print jobs anytime", 100, "description", "Printing"),
        RewardItem("rew-2", "Free Spiral Binding", "Get high quality plastic coil spiral on any document", 80, "menu_book", "Binding"),
        RewardItem("rew-3", "Campus Cafe Coffee", "Free cappuccino at Library Cafe", 150, "local_cafe", "Perks"),
        RewardItem("rew-4", "Priority Pass (3 Uses)", "Skip 10 queue spots automatically", 200, "bolt", "Queue")
    )
}
