<style scoped>
    /*.expand-row{*/
        /*margin-bottom: 16px;*/
    /*}*/
</style>
<template>
    <div>
        <Row class="expand-row">
            <Col span="4">
                <span class="expand-key">应用描述: </span>
                <span class="expand-value">{{ row.appDesc }}</span>
            </Col>
            <Col span="4">
                <span class="expand-key">应用创建时间: </span>
                <span class="expand-value">{{ row.appCreateTime }}</span>
            </Col>
            <Col span="4">
                <span class="expand-key">应用最后修改时间: </span>
                <span class="expand-value">{{ row.appUpdateTime }}</span>
            </Col>
            <Col span="4">
                <span class="expand-key">配置最后修改人: </span>
                <span class="expand-value">{{ row.profileModifyMan }}</span>
            </Col>
            <Col span="4">
                <span class="expand-key">配置创建时间: </span>
                <span class="expand-value">{{ row.profileCreateTime }}</span>
            </Col>
            <Col span="4">
                <span class="expand-key">配置最后修改时间: </span>
                <span class="expand-value">{{ row.profileUpdateTime }}</span>
            </Col>
        </Row>
    </div>
</template>
<script>
    export default {
        props: {
            row: Object
        }
    };
</script>