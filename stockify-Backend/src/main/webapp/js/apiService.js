window.ApiService = {
    downloadProductBarcode: function (productId) {
        return fetch('/report/barcode/' + productId, {method: 'GET'}).then(function (response) {
            if (!response.ok) {
                throw new Error('Unable to download barcode');
            }
            return response.blob();
        });
    }
};
