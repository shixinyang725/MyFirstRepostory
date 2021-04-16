var vm = new Vue({
    el:'#meundiv',
    data:{
       meunlist:[],
       page:{},
       pageNum:1,
       pageSize:3,
       pid:1,
       entity:{}
    },
    methods:{

        paging:function(pageNum){
            this.pageNum = pageNum;
            this.getMeunListByPid(this.pid);
        },

        getMeunListByPid:function (pid) {
            this.pid = pid;
            axios.get("../meun/getMeunListByPid.do?pageNum="+this.pageNum+"&pageSize="+this.pageSize+"&pid="+pid).then(function (response) {
                this.vm.page = response.data;
                this.vm.meunlist = response.data.list;
                this.vm.pageNum = response.data.currentPage;
                this.vm.pageSize = response.data.pageSize;
            })
        },

        toAddMeun:function () {
            this.entity={};
            document.getElementById("meunupdatediv").style.display="block";
        },

        saveMeun:function () {
            this.entity.pid = this.pid;
            axios.post("../meun/saveMeun.do",this.entity).then(function (response) {
                if(response.data.flag){
                    this.vm.getMeunListByPid(this.vm.pid);
                    document.getElementById("meunupdatediv").style.display="none";
                }else{
                    alert(response.data.msg);
                }
            })
        },

        //删除
        deleteMeunById:function (id) {
            axios.get("../meun/deleteMeunById.do?id="+id).then(function (response) {
                if(response.data.flag){
                    alert(response.data.msg)
                    this.vm.getMeunListByPid(this.vm.pid);
                }else{
                    alert(response.data.msg)
                }
            })
        }
    }

});
vm.getMeunListByPid(1);