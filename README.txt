


一、测试demo前注意事项
    1、使用本DEMO之前，请先自行建立数据库qd_product_center，导入SQL配置：qd_product_center.sql
    2、修改src/main/resource/qdevelop-database.xml

二、qdevelop-service中包含通用接口一览：
    1、通用查询接口：/svr/ajax/query 
    参考实现类：cn.qdevelop.service.common.api.AjaxQuery

    2、通用总计查询接口：/svr/ajax/queryCount
    参考实现类：cn.qdevelop.service.common.api.AjaxQueryCount

    3、通用基于bootstrap中的dataTables表格结构数据查询接口：/svr/ajax/dataTables
    参考实现类：cn.qdevelop.service.common.api.DataTables

    4、通用表单提交处理接口：/svr/ajax/formCommit
    参考实现类：cn.qdevelop.service.common.api.FormCommit

    5、通用文件上传接口：/svr/ajax/fileUpload
    参考实现类：cn.qdevelop.service.common.file.FileUploadStore

    
