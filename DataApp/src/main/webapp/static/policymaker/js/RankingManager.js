$(function(){

    const rankingController = new RankingController($('#rankingContainer'), $('#recalculateBtn'));
    rankingController.addListener();
    rankingController.loadData();

    function RankingController(_container, _recalculateBtn){
        var self = this;
        this.container = _container;
        this.recalculateBtn = _recalculateBtn;


        this.loadData = function(){
            this.container.html("");
            districtId = self.getDistrictId();
            $.ajax({
                type: "GET",
                url: "../api/data/ranking",
                data: "districtId=" + districtId,
                contentType: "application/json; charset=utf-8",
                accept: "application/json",
                dataType: "json",
                success: function(json){
                    json.forEach(r => {
                        self.createNode(r);
                    });
                },
                error: function(e) {
                    self.container.append('<div class="alert alert-danger mt-2">An error occurred. Please try later</div>');
                }
            });
        }

        this.recalculateDeviance = function(){
            this.container.html("");
            self.prepareDistrictId();
            $.ajax({
                type: "GET",
                url: "../api/data/ranking/recalculate",
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: $("#recalculateForm").serialize(),
                success: function(json){
                    json.forEach(r => {
                        self.createNode(r);
                    });
                },
                error: function(e) {
                    self.container.append('<div class="alert alert-danger mt-2">An error occurred. Please try later</div>');
                }
            });
        }

        this.createNode = function(ranking) {
            var position = $('<td/>').addClass('text-center align-middle').text(ranking.position);
            var area = $('<strong/>').text(ranking.district);
            var content = $('<p/>').text(ranking.zone);
            var contentZone = $('<td/>').addClass('align-middle').append(area).append(content);
            var value = $('<p/>').text(ranking.value);
            var valueZone = $('<td/>').addClass('align-middle').append(value);
            var arrow = $('<i/>').addClass('fas fa-arrow-up');
            var arrowZone = $('<td/>').addClass('text-center align-middle').append(arrow);
            var tableNode = $('<tr/>').append(position).append(contentZone).append(valueZone).append(arrowZone);
            this.container.append(tableNode);
        }

        this.addListener = function () {
            self.recalculateBtn.on("click", function () {
                self.recalculateDeviance();
            });
        }

        this.prepareDistrictId = function(){
            $('#districtId').val(self.getDistrictId());
        }

        this.getDistrictId = function () {
            switch (areaOfResidence) {
                case "Adilabad":
                    return "19_1";
                case "Bhadradri Kothagudem":
                    return "22_2";
                case "Hyderabad":
                    return "21_1";
                case "Jagtial":
                    return "16_1";
                case "Jangoan":
                    return "20_2";
                case "Jayashankar Bhupalpally":
                    return "21_3";
                case "Jogulamba Gadwal":
                    return "21_4";
                case "Kamareddy":
                    return "14_2";
                case "Karimnagar":
                    return "18_2";
                case "Khammam":
                    return "20_1";
                case "KumurambheemAsifabad":
                    return "22_1";
                case "Mahabubabad":
                    return "19_4";
                case "Mahabubnagar":
                    return "21_5";
                case "Mancherial":
                    return "14_1";
                case "Medak":
                    return "19_3";
                case "Medchal Malkajgiri":
                    return "17_1";
                case "Mulugu":
                    return "15_2";
                case "Nagarkurnool":
                    return "21_6";
                case "Nalgonda":
                    return "14_3";
                case "Narayanpet":
                    return "23_1";
                case "Nirmal":
                    return "14_5";
                case "Nizamabad":
                    return "19_2";
                case "Peddapalli":
                    return "18_1";
                case "Rajanna Sircilla":
                    return "20_4";
                case "Rangareddy":
                    return "20_3";
                case "Sangareddy":
                    return "15_1";
                case "Siddipet":
                    return "17_2";
                case "Suryapet":
                    return "17_3";
                case "Vikarabad":
                    return "23_2";
                case "Wanaparthy":
                    return "15_3";
                case "Warangal Rural":
                    return "14_4";
                case "Warangal Urban":
                    return "21_2";
                case "Yadadri Bhuvanagiri":
                    return "23_3";
                default:
                    return "";
            }
        }
    }

});