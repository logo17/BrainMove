package com.loguito.brainmove.models.remote

import java.util.*

data class Payment(var userId: String = "", var description: String = "", var total: Double = 0.0, var dueDate: Date = Date(), var paymentDate: Date = Date())