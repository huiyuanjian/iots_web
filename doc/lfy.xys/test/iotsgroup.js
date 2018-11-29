var vm = new Vue({
	el : '#rrapp',
	data : {
		showList : true,
		title : null,
		iotsGroup : {
			parentid : 0
		}
	},
	methods : {
		add : function() {
			vm.showList = false;
			vm.title = "新增";
			vm.iotsGroup = {};
		},
		update : function(event) {
			var id = getSelectedRow();
			if (id == null) {
				return;
			}
			vm.showList = false;
			vm.title = "修改";

			vm.getInfo(id)
		},
		saveOrUpdate : function(event) {
			var url = vm.iotsGroup.id == null ? "iots/iotsgroup/save"
					: "iots/iotsgroup/update";
			$.ajax({
				type : "POST",
				url : baseURL + url,
				contentType : "application/json",
				data : JSON.stringify(vm.iotsGroup),
				success : function(r) {
					if (r.code === 0) {
						alert('操作成功', function(index) {
							vm.reload();
						});
					} else {
						alert(r.msg);
					}
				}
			});
		},
		del : function(event) {
			var ids = getSelectedRows();
			if (ids == null) {
				return;
			}

			confirm('确定要删除选中的记录？', function() {
				$.ajax({
					type : "POST",
					url : baseURL + "iots/iotsgroup/delete",
					contentType : "application/json",
					data : JSON.stringify(ids),
					success : function(r) {
						if (r.code == 0) {
							alert('操作成功', function(index) {
								$("#jqGrid").trigger("reloadGrid");
							});
						} else {
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo : function(id) {
			$.get(baseURL + "iots/iotsgroup/info/" + id, function(r) {
				vm.iotsGroup = r.iotsGroup;
			});
		},
		reload : function(event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
				page : page
			}).trigger("reloadGrid");
		}
	}
});

/**
 * jqgrid tree
 */
var Iots = {
	id : "iotsTable",
	table : null,
	layerIndex : -1
};

/**
 * 初始化表格的列
 */
Iots.initColumn = function() {
	var columns = [ {
		field : 'selectItem',
		radio : true
	}, {
		title : 'ID',
		field : 'id',
		visible : false,
		align : 'center',
		valign : 'middle',
		width : '80px'
	}, {
		title : '分组名称',
		field : 'name',
		align : 'center',
		valign : 'middle',
		sortable : true,
		width : '180px'
	}, {
		title : '父ID',
		field : 'parentid',
		align : 'center',
		valign : 'middle',
		sortable : true,
		width : '100px'
	} ];
	return columns;
};

$(function() {
	var colunms = Iots.initColumn();
	var table = new TreeTable("iotsTable", baseURL + "iots/iotsgroup/list1",
			colunms);
	table.setExpandColumn(1);
	table.setIdField("id");
	table.setCodeField("id");
	table.setParentCodeField("parentid");
	table.setExpandAll(false);
	table.init();
	Iots.table = table;

});

/**
 * zTree
 * 
 */
var setting = {
	data : {// 表示tree的数据格式
		simpleData : {
			enable : true,// 表示使用简单数据模式
			idKey : "id",// 设置之后id为在简单数据模式中的父子节点关联的桥梁
			pIdKey : "parentid",// 设置之后pid为在简单数据模式中的父子节点关联的桥梁和id互相对应
			rootId : "0"// pid为null的表示根节点
		},
		key : {
			name : "name"
		}
	},
	view : {
		selectedMulti : false
	}
};

var ztree;
$.get(baseURL + "iots/iotsgroup/list1", function(r) {
	ztree = $.fn.zTree.init($("#zTree"), setting, r);
	jQuery("#zTreeLayer");
//	layer.open({
//        type: 1,
//        offset: '50px',
//        skin: 'layui-layer-molv',
//        title: "测试树",
//        area: ['300px', '450px'],
//        shade: 0,
//        shadeClose: false,
//        content: jQuery("#zTreeLayer"),
//        btn: ['确定', '取消'],
//        btn1: function (index) {
//            var node = ztree.getSelectedNodes();
//            //选择上级奖品
////            vm.prizePoolInfo.prizeId = node[0].prizeId;
////            vm.prizePoolInfo.prizeName = node[0].prizeName;
//            layer.close(index);
//        }
//    });
});

