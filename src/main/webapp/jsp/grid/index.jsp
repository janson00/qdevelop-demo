<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:include page="../import-css.jsp" flush="true" />
<body class="hold-transition skin-blue sidebar-mini">
	<jsp:include page="../import-header.jsp" flush="true" />
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>
				<small>表格测试</small>
			</h1>
			<ol class="breadcrumb">
				<li><a href="#"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li class="active">表格测试</li>
			</ol>
		</section>

		<!-- Main content -->
		<section class="content">
			<div class="box box-default color-palette-box">
		        <div class="box-header with-border table-bordered">
		          <h3 class="box-title"><i class="fa fa-table"></i> 数据表格</h3>
		        </div>
		        <div class="box-body">
		        	<!-- <table  class="table table-striped" cellspacing="0" width="100%">
		        		<thead>
							<tr>
								<td colspan="5" style="background-color:#efefef">
									<input type="text" id='epp_name' title="职位搜索" value="职位搜索"  class="search-input-text" style="width:60px"> 
									&nbsp; <span class="fa fa-search" id="searchSubmit" style="cursor:pointer" title="点击搜索结果"></span>
								</td>								
							</tr>
						</thead>
		        	</table> -->
					<table id="example" class="table table-striped table-bordered table-hover" cellspacing="0" width="100%">
						<thead>
							<tr>
								<th><b>职位</b></th>
								<th><b>佣金</b></th>
								<th><b>描述</b></th>
								<th><b>状态</b></th>
								<th><b>状态</b></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</section>
		<!-- /.content -->
	</div>
	<jsp:include page="../import-footer.jsp" flush="true" />
	<div class="control-sidebar-bg"></div>
</body>
<jsp:include page="../import-javascript.jsp" flush="true" />
<script type="text/javascript" src="<%=request.getContextPath()%>/svr/conf/ep_cash_amount_apply_status_map"></script>

<script type="text/javascript">
    $(function () {
    	
    	/**
    	*	{
    	*		url:'',
    	*		columns:[{field:'EPP_NAME',title:'职位名称',sortable:false,width:180,hidden:false,align:'center'}]	
    	*	}
    	*/
        var table = $("#example").easyGrid({
        	data:{index:'productsWithUserInfo',status:1,order:'price asc'},
        	columns: [
                      {"field":"product_name",title:'商品名','sortable':false}
                      ,{ "field": "price",title:'价格','sortable':true,'createdCell':function (td, cellData, rowData, row, col) {
                    	  $(td).html('<span style="color:green">'+cellData+'</span>');
                      }}
                      ,{ "field": "store",title:'库存',hidden:true,"formatter": function ( data, type, rowData, meta ) {
                    	  //console.log(meta.settings.oAjaxData.columns[meta.col].data);
                          return  data;  
                      }}
                      ,{ "field":"status",title:'状态','sortable':true,hidden:false}
                      ,{ "field":"user_name",title:'用户名','sortable':false,hidden:false}
                      ,{ "field":"mobile",title:'手机号','sortable':false,hidden:false}
                      ,{"field":'pid',title:'操作','createdCell':function (td, cellData, rowData, row, col) {
                    	  $(td).html('<input value="del" name="del" type="button"/>');
                      }}
                  ]
        	,search:{
        		'product_name':{title:'商品名'},
        		'status':{title:'状态',width:'100px','type':'select',option:[{text:'停用',value:0},{text:'正常',value:1},{text:'暂停',value:2}]}
        	}
        });
    	
    
    	/* $('#searchSubmit').click(function(){
    		var args={};
    		gridParams = table.context[0].oAjaxData.params;
    		$(this).parent().children().each(function(){
    			var val = $(this).val();
    			var title=$(this).attr('title');
    			if(typeof val == 'string' && typeof $(this).attr('id') == 'string' && val.indexOf(title) == -1 && val.length > 0){
    				gridParams[$(this).attr('id')] = val;	
    			}else{
    				delete gridParams[$(this).attr('id')];
    			}
    		});
    		table.ajax.reload();
    	}); */
 
        /*  setTimeout( function () {
    	   //手动 reload table
    	    table.ajax.reload();
    	    console.log(table.ajax);
    	}, 5000 );   */
    });
</script>