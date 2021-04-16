var vm = new Vue({
    el:'#logindiv',
    data:{
        ub:{}
    },
    methods:{
        getLogin:function () {
            axios.post("/user/getLogin.do",this.ub).then(function (response) {
                if(response.data.flag){
                    alert(response.data.msg)
                    location.href="../pages/main.html";
                }else {
                    alert(response.data.msg);
                }
            })
        }
    }
})