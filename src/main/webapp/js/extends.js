$(function () {
	/**
	*	{
	*		
	*		columns:[{field:'EPP_NAME',title:'职位名称',sortable:false,width:180,hidden:false,align:'center'}]	
	*	}
	*/
$.fn.easyGrid = function(_opts){
    		var _column = [];
    		for(var i=0;i<_opts.columns.length;i++){
    			var t = {};
    			t['data'] = _opts.columns[i]['field'];
    			t['orderable'] = typeof _opts.columns[i]['sortable'] == 'undefined' ? false : _opts.columns[i]['sortable'];
    			t['searchable'] = typeof _opts.columns[i]['searchable'] == 'undefined' ? false : _opts.columns[i]['searchable'];
    			t['sClass'] = typeof _opts.columns[i]['align'] == 'center' ? false : _opts.columns[i]['align'];
    			if(t['searchable']){
    				t['search']={'regex':true,value:''};
    			}
    			if(typeof _opts.columns[i]['createdCell'] == 'function' ){
    				t["createdCell"] = _opts.columns[i]['createdCell'];
    			}
    			if(typeof _opts.columns[i]['formatter'] == 'function' ){
    				t["render"] = _opts.columns[i]['formatter'];
    			}else{
    				t["render"] = function(data, type, rowData, meta){
    					var key = meta.settings.oAjaxData.columns[meta.col].data;
    					if(typeof key == 'string' && typeof rowData["__"+key] != 'undefined'){
    						return rowData["__"+key];
    					}
    					return data;
    				}
    			}
    			if(typeof _opts.columns[i]['hidden'] != 'undefined' && _opts.columns[i]['hidden']){
    				t['visible'] = false;
    			}
    			if(typeof _opts.columns[i]['title'] != 'undefined'){    				
    				t['title'] = _opts.columns[i]['title'];
    			}
    			_column.push(t);
    		};
    		_opts.columns = _column;
    		var params = $.extend({},_opts.data);
    		var settings = $.extend({  
            	language:{
    	            "sProcessing": "处理中...",
    	            "sLengthMenu": "每页 _MENU_ 项",
    	            "sZeroRecords": "没有匹配结果",
    	            "sInfo": "当前显示第 _START_ 至 _END_ 项。<a id='countQuery' href='javascript:console.log("+JSON.stringify(params)+");' >[查看总计]</a>",//，共 _TOTAL_ 项
    	            "sInfoEmpty": "当前显示第 0 至 0 项，共 0 项",
    	            "sInfoFiltered": "",//"(由 _MAX_ 项结果过滤)",
    	            "sInfoPostFix": "",
    	            "sSearch": "搜索:",
    	            "sUrl": "",
    	            "sEmptyTable": "表中数据为空",
    	            "sLoadingRecords": "载入中...",
    	            "sInfoThousands": ",",
    	            "oPaginate": {
    	                "sFirst": "首页",
    	                "sPrevious": "上页",
    	                "sNext": "下页",
    	                "sLast": "末页",
    	                "sJump": "跳转"
    	            },
    	            "oAria": {
    	                "sSortAscending": ": 以升序排列此列",
    	                "sSortDescending": ": 以降序排列此列"
    	            }
            	},  
                autoWidth: false,  //禁用自动调整列宽
                processing: true,  //隐藏加载提示,自行处理
                serverSide: true,  //启用服务器端分页
                searching: false,  //禁用原生搜索
                bDeferRender:false,
                "bJQueryUI":false,
                "bLengthChange":false,
                "bScrollInfinite":false,
                "sScrollX":"100%",
                "sScrollY":"100%",
                order: [],  //取消默认排序查询,否则复选框一列会出现小箭头
                renderer: "bootstrap",  //渲染样式：Bootstrap和jquery-ui
                pagingType: "simple_numbers",  //分页样式：simple,simple_numbers,full,full_numbers
                select: true,
    		    ajax: function (data, callback, settings) {
    		    	if(typeof data['params'] == 'Object'){
    		    		$.extend(params,data['params']);
    		    	}
                    //封装请求参数
                    params.page_size = data.length;//页面显示记录条数，在页面显示每页显示多少项的时候
                    params.page = (data.start / data.length)+1;//当前页码
                    params.draw = data.draw;
                    if(typeof data.order != 'undefined' && data.order.length>0){
                    	var cidx = data.order[0]["column"];
                    	var dir = data.order[0]["dir"];
                    	var columnName= data.columns[cidx].data;
                    	params.order = columnName+" "+dir
                    }
                 //   console.log(params);
                    data['params'] = params;
                    //ajax请求数据
                    $.ajax({
                        type: "post",
                        url: getDomain()+'/svr/ajax/dataTables.json',
                        cache: false,  //禁用缓存
                        data: params,  //传入组装的参数
                        dataType: "json",
                        success : function (result) {
                        	if(typeof callback == 'function'){
                        	  callback(result);
                        	}
                        }
                    });
                },columns:[]},_opts);
    		//console.log(_column);
    		var _grid = this.dataTable(settings).api();
    		if(typeof _opts.search == 'object'){
    			$(this).gridSearch(_opts.search,_grid);
    		}
    		
    		return _grid;
 };
    	
 $.fn.gridSearch = function(_opts,_grid){
	 var searchContain = '<table  class="table table-striped" cellspacing="0" width="100%"> <thead> <tr> <td colspan="5" style="background-color:#efefef">';
	 for(var column in _opts){
		 var conf = $.extend({
			 title:'搜索',
			 equals:true,
			 type:'text',
			 width:'80px',
			 style:'',
			 'class':''
		 },_opts[column]);
		 conf.type = conf.type.toUpperCase();
		 _opts[column] = conf;
		switch(conf.type){
			case 'TEXT':
				searchContain += '<input type="text" id="'+column+'" title="'+conf.title+'" value="'+conf.title+'" equals="'+conf.equals+'"  class="search '+conf['class']+'" style="width:'+conf.width+';'+conf.style+'"/>';
				break;
			case 'SELECT':
				searchContain += '<select id="'+column+'" title="'+conf.title+'" equals="true"  class="search '+conf['class']+'" style="width:'+conf.width+';'+conf.style+'"></select>';
				break;
		}
	 }
	 
	// searchContain += '<input type="text" id="amount" title="佣金搜索" value="佣金搜索"  class="search-input-text" style="width:80px;margin-right:5px">';

	 searchContain += ' <span class="fa fa-search" id="searchSubmit" style="cursor:pointer" title="点击搜索结果"></span> </td> </tr> </thead> </table>';
	 var target = $(this).closest('.dataTables_wrapper').before(searchContain).prev();
	 target.find(':input').click(function(){
		 if($(this).attr('type') == 'text' && $(this).val() == $(this).attr('title')){
				$(this).val('');
		 }
		 //console.log($(this).val());
	 }).mouseout(function(){					
			if($(this).val() == ''){
				$(this).val($(this).attr('title'));
			}
	}).each(function(){//初始化各类型数据填充
		var key = $(this).attr('id');
		//alert(key);
		if(typeof _grid.context[0].oAjaxData.params[key] != 'undefined'){
			$(this).val(_grid.context[0].oAjaxData.params[key]);
		}
		var conf = _opts[key];
		switch(conf.type){
		case 'SELECT':
			conf.first = conf.title+'-全部';
			$(this).option(conf);
			//console.log(conf);
			break;
		default :				
	   }
	});
	 
	target.find('.fa-search').click(function(){
		var setting = $(this).data('setting');	
		gridParams = _grid.context[0].oAjaxData.params;
		var data = $(this).parent().getData();
		for(var key in setting){
			var conf = setting[key];
			var val = data[key];
			if(typeof val == 'string' && val != conf.title){
				if(conf['equals']||val.indexOf('*')>-1){
					_grid.context[0].oAjaxData.params[key] = val;
				}else{
					_grid.context[0].oAjaxData.params[key] = '%'+val+'%';
				}
			}else{
				delete _grid.context[0].oAjaxData.params[key];
			}
		}
		//console.log(data);
		_grid.ajax.reload();
	}).data('setting',_opts);
	
	target.setData(_grid.context[0].oAjaxData.params);
 };
 
 $.fn.getData = function(fullData,isSensitiveCheck,isCheckScript){
		if(typeof isCheckScript == 'undefined')isCheckScript=true;
		var tmpJson = {};
		var getMultiSelectValue = function(key,_select){
			_select.find('option').each(function(){
				if(this.selected){
					if(tmpJson[key]==null)
						tmpJson[key] = $(this).val();
					else
						tmpJson[key] += ';'+$(this).val();
				}
			});
		};
		this.find(':input').each(function(){
			_thisType = $(this).attr('type');
			var dataKey = $(this).attr('name')||$(this).attr('id');
			if(dataKey==null || dataKey =='' || $(this).is('.combo-value'))return;
			if($(this).is('.combo-f')){
				if($(this).combo('options').multiple)
					tmpJson[dataKey] = $(this).combo('getValues').join(',');
				else
					tmpJson[dataKey] = $(this).combo('getValue');
			}else if($(this).is('.combobox-f')){
				if($(this).combobox('options').multiple){//XXX
					tmpJson[dataKey] = $(this).combobox('getValues').join(',');
				}else{
					tmpJson[dataKey] = $(this).combobox('getValue');
				}
			}else if($(this).is('.combotree-f')){
				if($(this).combotree('options').multiple){//XXX
					tmpJson[dataKey] = $(this).combotree('getValues').join(',');
				}else{
					tmpJson[dataKey] = $(this).combotree('getValue');
				}
				//tmpJson[dataKey] = $(this).combotree('getValue');
			}else if($(this).is('.datebox-f')){						
				tmpJson[dataKey] = $(this).datebox('getValue');
			}else if($(this).is('.datetimebox-f')){
				tmpJson[dataKey] = $(this).datetimebox('getValue');
			}else if($(this).is('.easyui-numberbox')){
				tmpJson[dataKey] = $(this).numberbox('getValue');
			}else{
				if(_thisType == 'radio' || _thisType == 'checkbox'){
					if(this.checked){
						if(tmpJson[dataKey]==null){
							tmpJson[dataKey] = $(this).val();
						}else{
							tmpJson[dataKey] += ';'+$(this).val();
						}
					}
				}else if(_thisType=="select-multiple"){
					getMultiSelectValue(dataKey,$(this));
				}else if(_thisType=="select-one"){//XXX  baoft 
					var tVal = $(this).val();
					if(tVal!=null&&tVal.length>0){
						tmpJson[dataKey] = tVal;
					}else{						
						tmpJson[dataKey] = tVal;						
					};

				}else{
					// type attribute of input tag [button;checkbox;file;hidden;password;radio;reset;submit;text]
					if('button;reset;submit'.indexOf(_thisType)==-1){
						if(fullData){
							var tVal = $(this).val();
							if(tmpJson[dataKey]==null)
								tmpJson[dataKey] = tVal;
							else tmpJson[dataKey] = tmpJson[dataKey]+';'+tVal;
						}else{
							var tVal = $(this).val();
							if(tVal!=null&&tVal.length>0){
								if(tmpJson[dataKey]==null)
									tmpJson[dataKey] = tVal;
								else tmpJson[dataKey] = tmpJson[dataKey]+';'+tVal;
							}				
						}
					}
				}
			};
			if(!fullData && typeof tmpJson[dataKey] == 'string' && tmpJson[dataKey].replace(/^ | $/g,'').length ==0)delete tmpJson[dataKey];
		});

		//if(isCheckScript)tmpJson = checkScript(tmpJson);//XXX 暂时不需要处理过滤JS文本
		return tmpJson;
	};
	
	$.fn.setData = function(_data,_formatParam){
		var formatParam = $.extend({
			clear:true,//情况表单所有内容
			isQDevelopData:false,//表单所有数据KEY均为大写
			isFormOnly:true,//仅仅将数据展示在表单内
			readOnly:false,//将表单设置成只查看状态
			filter:[],//过滤不操作的表单内容
			format:null//数据值设置自定义
		},_formatParam);
		//if(formatParam.clear)$(this).form('clear');//采用easy-ui的form clear 方法清除数据
		var isIe6 = isIE6();
		var setMultiSelectValue = function(_select,value){
			_select.find('option').each(function(){
				if(value.indexOf(';'+$(this).val()+';')>-1){
					if(isIe6){
						try{
							this.selected = true;
						}catch(e){}					
					}else{
						$(this).attr('selected','selected') ;
					}
				}
			});
			_select.change();
		};
		var data = {};
		if(formatParam.isQDevelopData){
			for(each in _data)data[each.toUpperCase()] = _data[each];
		}else{
			data = $.extend({},_data);
		};
		if(data == null)return;
		var multiKey = [];
		this.find(':input').each(function(){
			var _dValue = '';
			_thisId = $(this).attr('id');
			_thisName = $(this).attr('name');
			_thisType = $(this).attr('type');
			if((!_thisId&&!_thisName)||_thisType=='button')return;
			key = _thisId||_thisName;
			if(formatParam.isQDevelopData)key = key.toUpperCase();
			_dValue = data[key];
			if(_dValue == null||formatParam.filter.contains(key))return;

			if(formatParam.format!=null&&
					(typeof formatParam.format[_thisType] == 'function'||typeof formatParam.format[_thisId] == 'function')){
				if(typeof formatParam.format[_thisType] == 'function'){
					formatParam.format[_thisType](this,_dValue);
				}else{formatParam.format[_thisId](this,_dValue);};
			}else{
				if(_dValue!=null){
					if($(this).is('.combo-f')){
						if($(this).combo('options').multiple)
							$(this).combo('setValues',typeof _dValue == 'object'?_dValue:_dValue.split(","));
						else
							$(this).combo('setValue',_dValue);
					}if($(this).is('.combobox-f')){
						if($(this).combobox('options').multiple)
							$(this).combobox('setValues',typeof _dValue == 'object'?_dValue:_dValue.split(","));
						else
							$(this).combobox('setValue',_dValue);
					}else if($(this).is('.combotree-f')){			
						if($(this).combotree('options').multiple)
							$(this).combotree('setValues',typeof _dValue == 'object'?_dValue:_dValue.split(","));
						else
							$(this).combotree('setValue',_dValue);
					}else if($(this).is('.datebox-f')){						
						$(this).datebox('setValue',_dValue);
					}else if($(this).is('.datetimebox-f')){
						$(this).datetimebox('setValue',_dValue);
					}else if($(this).is('.easyui-numberbox')){
						$(this).numberbox('setValue',_dValue);
					}else{						
						if(_thisType == 'radio' || _thisType == 'checkbox'){
							var checkValue = ';'+_dValue+';';
							if(_dValue!=null&&checkValue.indexOf(';'+this.value+';') > -1)
								$(this).attr('checked','checked');	
						}else if(_thisType=="select-one"||_thisType=="select-multiple"){
							var checkValue = ';'+_dValue+';';
							if(_dValue!=null)setMultiSelectValue($(this),checkValue);				
						}else {
							if(typeof _dValue =='number' ||( typeof _dValue =='string' && _dValue.toUpperCase()!='NULL'))$(this).val(_dValue);
						}
					}
				}else{
					$(this).val('');
				}
			};
			if(!formatParam.isFormOnly)multiKey.push(key);			
		});

		if(!formatParam.isFormOnly){
			for(var i=0;i<multiKey.length;i++){
				delete data[multiKey[i]];
			};
			var _targetForm = this;
			for(each in data){
				$('#'+(formatParam.isQDevelopData?each.toLowerCase():each),_targetForm).html(data[each]);
			}
		};

		if(formatParam.readOnly){
			this.find(':input').each(function(){
				_thisId = $(this).attr('id');
				_thisName = $(this).attr('name');
				_thisType = $(this).attr('type');
				if(!_thisId&&!_thisName)return;
				if(formatParam.format!=null&&(typeof formatParam.format[_thisType] == 'function'
					||typeof formatParam.format[_thisId] == 'function')){
					if(typeof formatParam.format[_thisType] == 'function'){
						formatParam.format[_thisType](this);
					}else{
						formatParam.format[_thisId](this);
					}
				}else{
					$(this).attr('disabled','true');
					/*
					if(_thisType == 'radio' || _thisType == 'checkbox'){
					}else if(_thisType=="select-one"||_thisType=="select-multiple"){
						$(this).after($(this).find(':selected').text()).remove();
					}else{
						if(_thisType != 'hidden' && _thisType != 'button')$(this).after($(this).val()).remove();;
					}
					 */
				}
			});
			
		}else{
			this.find(':input').each(function(){
				$(this).removeAttr('disabled');
			});
		}
		return this;
	};
	
	/** 
	@constructor
	@description	动态Select元素值设定<br>
		支持静态值，ajax数据库查询值等数据源<br>
		支持SELECT联动
	@param {Object} opt 具体参数设定

	@example 
		基本值设定Demo：
		====静态数组====
		例1：select的text和value一致时可用
		$('#selectId').option({
			option:["1","2","3"]
		})

		例2：{key:value}结构数据可用
		$('#selectId').option({
			option:{value1:text1,value2:text2,value3:text3...}
		})

		例3：对象数组可用
		$('#selectId').option({
			option:[{text:'显示1',value:'值1'},{text:'显示2',value:'值2'}...]
		})

		例4:对象数组自定义显示结构
		$('#selectId').option({
			text:'name',value:'id',
			option:[{id:'显示1',name:'值1'},{id:'显示2',name:'值2'}...]
		})

		例5:对象数组自定义方法
		$('#selectId').option({
			text:function(data){//data为数组中的每个对象数据
				return '=='+data['name']+'==';
			},value:function(data){
				return data['id']+'%';
			},
			option:[{id:'显示1',name:'值1'},{id:'显示2',name:'值2'}...]
		})

		例6：ajax数据库获取option值
		$('#selectId').option({
			text:'ID',name:'VALUE',//该处值为大写，ajax方式获取数组中的对象的KEY均为大写
			data:{
				index:'sqlConfig',//获取
				//other sql param
			}
		})

		例7：联动操作(支持N级联动操作)
		$('#selectId').option({
			text:'ID',name:'VALUE',//该处值为大写，ajax方式获取数组中的对象的KEY均为大写
			data:{
				index:'sqlConfig',//获取
				//other sql param
			},
			filterParent:'#fatherDomId',//父选择框的ID 最多递归往上找5层找其父选择框
			filterParentWith:'pid'//父选择框选定值后，筛选该数组对象中key为pid的值和父类相等的值
		})

	 */
	$.fn.option = function(opt,param){
		if(typeof opt == 'string'){
			$.fn.option.methods[opt](this,param);
			return;
		};
		opt = $.extend(true,{text:'text',value:'value',__options:{textLength:15}},opt);
		if(opt==null)return;
		var _select = this;
		var _filterParentValue;
		var isFilterParent = typeof opt.filterParent!='undefined' && typeof opt.filterParentWith!='undefined' ? true:false;
		var _defaultKey = "default";
		// 初始话Select值
		initSelect = function(){
			if(opt.first){
				if(typeof opt.first == 'string'){
					_select.html('<option  value="">'+opt.first+'</option>');
				}else if(typeof opt.first == 'object'){	
					_select.html('<option value="'+(opt.first["value"])+'">'+(opt.first["text"])+'</option>');
				}else{
					_select.html('<option value="">请选择...</option>');
				}
			}else _select.html('');
		};
		__parseValue = function(key,data){
			if(typeof data == 'string') return data;
			else if(typeof key == 'string')return data[key];
			else if(typeof key == 'function')return key(data);
			else return "";
		};

		__checkFilterData = function(data){
			if(typeof opt.filter == 'undefined')return true;
			return opt.filter(data);
		};

		__formatOptions = function(_option){ 
			if(typeof _option.length == 'undefined'){//对象数据
				opt.__options[_defaultKey] = [];
				for(var value in _option){
					var text = _option[value];
					_data = {'value':value,'text':text};
					if(__checkFilterData(_data))
						opt.__options[_defaultKey] .push(_data);
				} 
			}else{//数组数据
				if(isFilterParent){
					$.each(_option,function(i,data){
						if(__checkFilterData(data)&&data[opt.filterParentWith]!=null){
							if(opt.__options[data[opt.filterParentWith]]==null)opt.__options[data[opt.filterParentWith]]=[];
							opt.__options[data[opt.filterParentWith]].push({'value':__parseValue(opt.value,data),'text':__parseValue(opt.text,data)});
						}
					});
				}else{
					opt.__options[_defaultKey] = [];	   
					$.each(_option,function(i,data){
						if(__checkFilterData(data))
							opt.__options[_defaultKey].push({'value':__parseValue(opt.value,data),'text':__parseValue(opt.text,data)});
					});
				}
			};
			delete opt.option;
			if(typeof opt.appendItem!='undefined'){

			}
		};

		__parseOption = function(indexValue){
			initSelect();
			indexValue = indexValue == null ? _defaultKey : indexValue;
			if(opt.__options[indexValue] == null)return;
			var _selectDom = _select[0];
			$.each(opt.__options[indexValue],function(i,data){
				if(data.text == null) data.text = data.value;
				if(opt.__options['textLength'] < data.text.length){
					_selectDom.options.add(new Option(data.text.substring(0,opt.__options['textLength'])+"...",data.value));
				}else{
					//console.log(data.text+' - '+data.value);
					_selectDom.options.add(new Option(data.text,data.value));
				}
			});
			_select.change(); 
		};

		__regParentFilter = function(){
			if(isFilterParent){
				var _filterParent=null;
				if(typeof opt.filterParent == 'object')
					_filterParent =  opt.filterParent;
				else if(typeof opt.filterParent == 'string'){
					_filterParent = $(_select).siblings(opt.filterParent);
					if(_filterParent.length==0){
						var deep = 0,_e = $(_select).parent();
						while(_filterParent.length==0&&deep<5){			
							_filterParent = _e.find(opt.filterParent);	
							_e = _e.parent();	
							deep++;			
						}
					}
				};
				_filterParent.change(function(){
					if(opt.first){
						if(typeof opt.first == 'string'){
							_select.html('<option  value="">'+opt.first+'</option>');
						}else if(typeof opt.first == 'object'){	
							_select.html('<option value="'+(opt.first["value"])+'">'+(opt.first["text"])+'</option>');
						}else{
							_select.html('<option value="">请选择...</option>');
						}
					}else _select.html('');
					var pVale = _filterParent.val();		
					if(pVale != null && pVale != ''&&opt.__options[pVale]!=null){
						var _selectDom = _select[0];
						if(typeof opt.beforeOptions =='function')opt.beforeOptions(_selectDom);
						$.each(opt.__options[pVale],function(i,data){
							if(opt.__options['textLength'] < data.text.length){
								_selectDom.options.add(new Option(data.text.substring(0,opt.__options['textLength'])+"...",data.value));
							}else{
								_selectDom.options.add(new Option(data.text,data.value));
							}
						});
						if(typeof opt.afterOptions =='function')opt.afterOptions(_selectDom);
					};
					_select.change(); 	
				});		
			}
		};

		if(typeof opt.option != 'undefined'){
			__formatOptions(opt.option);
			__parseOption();
			__regParentFilter();	
		}else if(typeof opt.db == 'object' ){
			if(typeof opt.url == 'string'){
				opt.db[url] = opt.url;
			}
			opt.db['async'] = false;
			ajaxQuery(opt.db,function(rb){
				__formatOptions(rb);
				__parseOption();
				__regParentFilter();	
			});
		}

	};
	$.fn.option.methods = {
			selected:function(_this,param){
				_select = _this.get(0);
				for(var i=0;i<_select.options.length;i++){				
					if(_select.options[i].value == param){
						_select.options[i].selected = true;
					}else  _select.options[i].selected = false;
				}
			}
	};
});


/** 
@constructor
@description ajax方式查询数据库
*/
function ajaxQuery(__opts,callback){
	var args = $.extend({
		scriptCharset:'UTF-8',
		ifModified:true,
		cache:false,
		url:getDomain()+'/svr/ajax/query',
		async:true,
		timeOut:60000,
		type:"POST",
		dataType:'json',
		success:function(r){
			if(r.success){
				callback(r.data,r);
			}else{
				alert(r.error);
			}
		},
		error:function(r,t,e){
			
		}
	},__opts);
	$.ajax(args);
};

/** 
@constructor
@description ajax方式更新数据库
*/
function ajaxUpdate(__opts,callback){
	var args = $.extend({
		scriptCharset:'UTF-8',
		ifModified:true,
		cache:false,
		url:getDomain()+'/svr/ajax/formCommit',
		async:true,
		timeOut:60000,
		type:"POST",
		dataType:'json',
		success:function(r){
			if(r.success){
				callback(r.data,r);
			}else{
				alert(r.error);
			}
		},
		error:function(r,t,e){
			
		}
	},__opts);
	$.ajax(args);
};

function isIE6() {
	try{
		if ($.browser.msie) {if ($.browser.version == "6.0") return true;}
	}catch(e){
	}
	return false;
};
function getDomain(){return typeof domain == 'string'?domain:'';};

Date.prototype.format = function(format){
	  var o = {
	    "M+" : this.getMonth()+1, //month
	    "d+" : this.getDate(),    //day
	    "h+" : this.getHours(),   //hour
	    "m+" : this.getMinutes(), //minute
	    "s+" : this.getSeconds(), //second
	    "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
	    "S" : this.getMilliseconds() //millisecond
	  };
	  if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
	    (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	  for(var k in o)if(new RegExp("("+ k +")").test(format))
	    format = format.replace(RegExp.$1,
	      RegExp.$1.length==1 ? o[k] :
	        ("00"+ o[k]).substr((""+ o[k]).length));
	  return format;
	};

	Array.prototype.contains = function(value){
		if(typeof value == 'object'){
			for(var i=0;i<this.length;i++){
				for(var j=0;j<value.length;j++){
					if(this[i] == value[j])return true;
				}
			}
		}else{
			for(var i=0;i<this.length;i++){
				if(this[i] == value)return true;
			}
		}
		return false;
	};

