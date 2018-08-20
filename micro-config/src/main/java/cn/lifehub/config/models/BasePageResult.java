package cn.lifehub.config.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BasePageResult<T> extends BaseResult<T> {

    /**
     * 分页信息
     */
    @JsonProperty("page_info")
    private PageInfo pageInfo;


    public BasePageResult() {

    }

    public BasePageResult(T data) {
        setData(data);
    }

    public BasePageResult(T data, PageInfo pageInfo) {
        setData(data);
        setPageInfo(pageInfo);
    }


    public BasePageResult(T data, Integer currentPage, Integer totalPage, Long totalCount) {
        setData(data);
        this.pageInfo = new PageInfo();
        this.pageInfo.setTotal(totalCount);
        this.pageInfo.setHasNext((currentPage + 1) < totalPage);
    }


    public BasePageResult(int code, String msg) {
        setCode(code);
        setMsg(msg);
    }

    public BasePageResult(int code, String msg, T data, PageInfo pageInfo) {
        setCode(code);
        setMsg(msg);
        setData(data);
        setPageInfo(pageInfo);
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    /**
     * 接口调用成功，传入需要返回的data数据
     *
     * @return
     */
    public static <T> BasePageResult<T> success(T data, PageInfo pageInfo) {
        return new BasePageResult<T>(data, pageInfo);
    }

    public static <T> BasePageResult<T> success(T data, Integer currentPage, Integer totalPage, Long totalCount) {
        return new BasePageResult<T>(data, currentPage, totalPage, totalCount);
    }

    /**
     * 接口调用成功，传入需要返回的data数据
     *
     * @param data
     * @return
     */
    public static <T> BasePageResult<T> success(T data) {
        return new BasePageResult<T>(data);
    }


    /**
     * 接口调用失败：不允许未登录用户调用此接口
     *
     * @return
     */
    public static <T> BasePageResult<T> notAllow() {
        return new BasePageResult<T>(CODE_NOT_ALLOW, "未经许可的用户", null, null);
    }

    /**
     * 接口调用失败：参数错误
     *
     * @return
     */
    public static <T> BasePageResult<T> parameterError() {
        return new BasePageResult<T>(CODE_PARAM_ERR, "参数解析错误", null, null);
    }

    /**
     * 接口调用失败：参数错误
     *
     * @return
     */
    public static <T> BasePageResult<T> parameterError(String msg) {
        return new BasePageResult<T>(CODE_PARAM_ERR, msg, null, null);
    }

    /**
     * 接口调用失败：服务器异常
     *
     * @return
     */
    public static <T> BasePageResult<T> serverError() {
        return new BasePageResult<T>(CODE_SERVER_ERR, "服务器异常", null, null);
    }

    /**
     * 接口调用失败：服务器异常
     *
     * @return
     */
    public static <T> BasePageResult<T> serverError(String msg) {
        return new BasePageResult<T>(CODE_SERVER_ERR, msg, null, null);
    }
}
