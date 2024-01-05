$(function () {

    const downloadController = new DownloadController( $('#downloadContainer'), $('#downloadBtn'), $('#datasetId'));
    downloadController.addListener();

    function DownloadController(_container, _downloadBtn, _dataSetIdText) {
        var self = this;
        this.container = _container;
        this.downloadBtn = _downloadBtn;
        this.dataSetIdText = _dataSetIdText;

        this.loadData = function (dataSetId) {
            $.get('api/data/dataset/' + dataSetId, (json) => {
                if(json.length === 0) {
                    self.container.append('<div class="alert alert-danger">The dataSet selected is empty</div>');
                } else {
                    self.download(json);
                }
            }).fail((error) => {
                self.container.append('<div class="alert alert-danger">An error occurred. Please try later</div>');
            });
        }

        this.download = function (json) {
            var element = $('#downloaderHref');
            element.attr('href',"data:text/plain;charset=UTF-8," + encodeURIComponent(JSON.stringify(json)));
            element.attr('download', 'dataset.json');
            element.removeAttr('hidden');
        }

        this.addListener = function () {
            self.downloadBtn.on("click", function () {
                var dataSetId = self.dataSetIdText.val();
                self.loadData(dataSetId);
                });
        }
    }
});