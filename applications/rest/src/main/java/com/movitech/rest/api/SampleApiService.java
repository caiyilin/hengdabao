package com.movitech.rest.api;

import com.movitech.mbox.modules.sys.service.DictService;
import com.movitech.mbox.test.service.RestApplicationSampleService;
import com.movitech.rest.vo.SampleRequest;
import com.movitech.rest.vo.SampleResponse;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Alex.Chen
 */
@Component
@Path("/sample")
@Api(value="SampleApiService", description="Sample 服务")
public class SampleApiService {

    @Autowired
    private RestApplicationSampleService restApplicationSampleService;

    @Autowired
    private DictService dictService;

    @Path("/sampleRequest")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="测试方法")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "方法执行正确",response = SampleResponse.class ),
            @ApiResponse(code = 500, message = "方法执行错误")})
    public Response sampleRequest(SampleRequest sampleRequest){
        SampleResponse sampleResponse = new SampleResponse();
        if("1".equals(sampleRequest.getType())){
            sampleResponse.setMessage("你选择了种类1");
        }else if("2".equals(sampleRequest.getType())){
            sampleResponse.setMessage("你选择了种类2");
        }else{
            sampleResponse.setError("你选择了不存在的种类");
            return Response.status(500).entity(sampleResponse).build();
        }
        return Response.status(200).entity(sampleResponse).build();
    }

    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="获取Token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "方法执行正确",response = SampleResponse.class ),
            @ApiResponse(code = 500, message = "方法执行错误")})
    public Response login(SampleRequest sampleRequest){
        SampleResponse sampleResponse = new SampleResponse();
        if("44".equals(sampleRequest.getType())){
            sampleResponse.setMessage("token获取到："+restApplicationSampleService.createToken());
        }else{
            sampleResponse.setError("用户名错误");
        }
        return Response.status(200).entity(sampleResponse).build();
    }


    @Path("/typeList")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="获取字典的类型列表")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "方法执行正确",response = List.class ),
            @ApiResponse(code = 500, message = "方法执行错误")})
    public Response typeList(){
        List<String> result = dictService.findTypeList();
        return Response.status(200).entity(result).build();
    }


}
