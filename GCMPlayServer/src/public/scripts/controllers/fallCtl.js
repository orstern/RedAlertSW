'use strict';

missilesShobApp.controller('FallCtl', ['$scope', '$http', function ($scope, $http, ngMap) {
    $scope.citiesIds = [];
    $scope.addedCitiesIds = [];
    $scope.notificationTitle = "";

    $http.get('/cities').
    success(function (data, status, headers, config) {
        console.log("loaded cities");
        console.log(data);

        $scope.citiesIds = Object.keys(data);
        $scope.newCityId = $scope.citiesIds[200];
        $scope.cities = data;
    });
    
    $scope.sendNotification = function () {

        var addedCities = angular.copy($scope.addedCitiesIds);
        var title = angular.copy($scope.notificationTitle);
        var request = {
            cities: addedCities,
            title: title
        };
        console.log(request);
        $http.post('/sendNotification', request).
        success(function (data, status, headers, config) {
            $scope.addedCitiesIds = [];
            $scope.notificationTitle = "";
            alert("notification sent");
        });
    }
 }]);