/**
 * Created by Sagar on 12/4/2016.
 */

// Module for the demo

angular.module('splashDemo', ['ui.splash'])
    .controller('MainCtrl', ['$splash', function ($splash) {
        this.openSplash = function () {
            $splash.open({
                title: 'Welcome to Library Management System!',
                message: "Please enter details below"
            });
        };
    }]);

// Re-usable $splash module
angular.module('ui.splash', ['ui.bootstrap', 'ngAnimate'])
    .service('$splash', [
        '$uibModal',
        '$rootScope',
        function ($uibModal, $rootScope) {
            return {
                open: function (attrs, opts) {
                    var scope = $rootScope.$new();
                    angular.extend(scope, attrs);
                    opts = angular.extend(opts || {}, {
                        backdrop: false,
                        scope: scope,
                        templateUrl: 'splash/content.html',
                        windowTemplateUrl: 'splash/home1.ejs'
                    });
                    return $uibModal.open(opts);
                }
            };
        }
    ])
    .run([
        '$templateCache',
        function ($templateCache) {
            $templateCache.put('splash/home1.ejs',
                '<section modal-render="{{$isRendered}}" class="splash" modal-in-class="splash-open" ng-style="{\'z-index\': 1000, display: \'block\'}" ng-click="close($event)">' +
                '  <div class="splash-inner" ng-transclude></div>' +
                '</section>'
            );
            $templateCache.put('splash/content.html',
                '<form id="registrationform" name="regform" action="\homescreen\\homepage.html">' +
                '<div class="splash-content text-center">' +
                '<h1 ng-bind="title"></h1>' +
                '<p class="lead" ng-bind="message"></p>' +
                'Name:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" id="uname" style="background-color : #000000; border-radius:5px;" /><br><br>' +
                'Password:&nbsp;&nbsp;&nbsp;&nbsp; <input type="password" id="password" style="background-color : #000000; border-radius:5px;" /><br><br>' +
                'Email ID:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="email" id="email" style="background-color : #000000; border-radius:5px;" /><br><br>' +
                'Contact No: &nbsp;&nbsp;<input type="telephone" id="phone" style="background-color : #000000; border-radius:5px;" /><br>' +
                '<button class="btn btn-lg btn-outline" ng-bind="btnText || \'Register\'" ng-click="$close()"></button>' +
                '</div>' +
                '</form>' +
                '<button class="fa fa-times" style="margin-left: 900px;  margin-top: 120px;" ng-bind="btnText || \'Cancel\'" ng-click="$close()"></button>'
            );
        }
    ]);
