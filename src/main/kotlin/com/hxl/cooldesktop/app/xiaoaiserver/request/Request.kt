package com.hxl.cooldesktop.app.xiaoaiserver.request

import com.hxl.cooldesktop.app.xiaoaiserver.request.Intent

class Request{
     var intent: Intent?= null
     var is_monitor: Boolean?= null
     var locale: String?= null
     var request_id: String?= null
     var slot_info: SlotInfo?= null 
     var timestamp: Long?= null
     var type: Int?=null
 }