'use strict';
var missilesShobApp = angular.module('missilesShobApp', ['ngRoute', 'ngMap'])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/main.html',
                controller: 'MainCtrl'
            })
            .when('/Shelters', {
                templateUrl: 'views/shelters.html',
                controller: 'ShelterCtrl'
            })
           .when('/Falls', {
                templateUrl: 'views/falls.html',
                controller: 'FallCtrl'
            })
            .when('/Team', {
                templateUrl: 'views/team.html',
                controller: 'TeamCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });
    });