//某些固定的值，放入缓存中
window.DataUtils = {
		// 业务类型
		BusinessType : [ {
			"code" : "1",
			"value" : "group表"
		}, {
			"code" : "2",
			"value" : "IOServer表"
		}, {
			"code" : "3",
			"value" : "设备表"
		}, {
			"code" : "4",
			"value" : "变量表"
		}, {
			"code" : "5",
			"value" : "数据包"
		} ],
		test:{}
}

function a(){
	if ($.isEmptyObject(DataUtils.test)) {
		var urls = "sys/dict/queryListType/prize_type";
		
		$.ajax({
			async : false,
			url : baseURL + urls,
			success : function(r) {
				alert("是空，执行一次");
				DataUtils.test = r.list;
			}
		});
	}
}
//测试
//a();