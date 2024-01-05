$(function () {

    const downloadController = new DownloadController($('#filterBtn'), $('#districtId'), $('#filteredDataContainer'));
    downloadController.addListener();

    function DownloadController(_filterBtn, _districtId, _filteredDataContainer) {
        var self = this;
        this.filterBtn = _filterBtn;
        this.districtId = _districtId;
        this.filteredDataContainer = _filteredDataContainer;

        this.loadData = function (districtId) {
            self.filteredDataContainer.html("");
            $.get('api/data/filter/' + districtId, (json) => {
                if(json.length === 0) {
                    self.filteredDataContainer.parent().append('<div class="alert alert-danger">The dataSet selected is empty for this district</div>');
                } else {
                    self.createNode(json);
                }
            }).fail((error) => {
                self.filteredDataContainer.parent().append('<div class="alert alert-danger">An error occurred. Please try later</div>');
            });
        }

        this.createNode = function(json) {
            this.filteredDataContainer.text(JSON.stringify(json));
        }

        this.addListener = function () {
            self.filterBtn.on("click", function () {
                var districtId = self.districtId.val();
                $('#apiUrlContainer').html('');
                $('#apiUrlContainer').append($('<p/>').html('<strong>Api url:</strong> api/data/filter/'+districtId))
                self.loadData(districtId);
                });
        }
    }
});