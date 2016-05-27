/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

(function () {
    var app = angular.module('myApp', ['UserValidation']);

angular.module('UserValidation', []).directive('validPasswordC', function () {
    return {
        require: 'ngModel',
        link: function (scope, elm, attrs, ctrl) {
            ctrl.$parsers.unshift(function (viewValue, $scope) {
                var noMatch = viewValue !== scope.myForm.password.$viewValue;
                ctrl.$setValidity('noMatch', !noMatch);
            });
        }
    };
});
})();