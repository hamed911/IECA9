/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

(function () {
    var app = angular.module('stockmarket', []);
    var login = true;

    app.controller('MarketController', ['$scope', '$http', '$interval', function ($scope, $http, $interval) {
            
        $scope.isLogin = login;    
    
        $scope.symbolsQ = [];
        $scope.custumers = [];
        $scope.userInstList = [];
        $scope.activeContent = 'content.html';
        
        $scope.init = function () {
            $http({
                method: 'GET',
                url: 'getinstrument'

            }).success(function (data, status, headers, config) {
                if (typeof data !== 'string') {
                    $scope.symbolsQ = data;
                    //alert('INIT SYMBOL');
                }
                else
                    $scope.notification = data;

            }).error(function (data, status, headers, config) {
                alert('Error:' + data);
            });
        }

        $scope.init();

        $scope.userInst = function () {
            $http({
                method: 'GET',
                url: 'getcustomerins',
                params: { 'id': $scope.enteredID }
            }).success(function (data, status, headers, config) {
                $scope.userInstList = data;

            }).error(function (data, status, headers, config) {
                //alert('Error:' + data);
            });
        }

        var marketCtrl = this;
        $scope.session = null;
        $scope.notification = null;
        this.symbolsQ = [];
        this.closeNotificationBar = function () {
            $scope.notification = null;
        }
        
        $scope.roles = [];
        this.getRoles = function(){
            $http({
                method: 'GET',
                url: 'getroles'
                
            }).success(function (data, status, headers, config) {
                $scope.roles = data;

            }).error(function (data, status, headers, config) {
                //alert('Error:' + data);
            });
        }
        
        this.checked = function(urol){
           // alert(rol);
           // alert(urol);
//            angular.forEach(urol, function (value,key) {
//                alert(rol);
//                if (key === rol && value === true) {
//                    alert('-> '+rol);
//                    
//                     return true;
//                }
//            });
                    
            return urol;
        }
        
        this.getCustomers = function () { 
            
            this.getRoles();
            $http({
                method: 'GET',
                url: 'getcustomers'
            }).success(function (data, status, headers, config) {
                //console.log((typeof data));
                
                if (typeof data !== 'string') {
                    $scope.customers = data;         
                }
                else{
                    $scope.notification = data;
                }
            }).error(function (data, status, headers, config) {
                alert('Error:' + data);
            });
        }
        
        $scope.customer = [];
        this.selectCustomer = function (value) {
            $scope.customer = value;
        }
        
        this.saveRole = function (cus){
            alert(cus["roles2"]["admin"]);
        }
        
        this.doLogin = function () {
            $http({
                method: 'GET',
                url: 'getcustomer'
            }).success(function (data, status, headers, config) {
                //console.log((typeof data));
                
                if (typeof data !== 'string') {
                    $scope.session = data;
                    $scope.userInst();
                    login = true;
                }
                else{
                    $scope.notification = data;
                    alert(data);
                }
            }).error(function (data, status, headers, config) {
                alert('Error:' + data);
            });
        }
        
        this.doLogout = function () {
            $http({
                method: 'GET',
                url: 'logout'
            }).success(function (data) {
                window.location.reload();
            }).error(function (data) {
                alert('Error:' + data);
            });
        }
        
        this.doSignup = function(family,name,email,username,password,rpassword){
            
            if(password !== rpassword){
                alert('confirm password does not match');
                return;
            }
            $http({
                method: 'GET',
                url: 'add',
                params: { 'family': family, 'name': name, 'email':email, 'id':username, 'password' : password}
            }).success(function (data, status, headers, config) {
                //console.log((typeof data));
                
                if (typeof data !== 'string') {
                    alert(data);
                }
                else{
                    $scope.notification = data;
                    alert(data);
                }

            }).error(function (data, status, headers, config) {
                alert('Error:' + data);
            });
        
        }


        $scope.symbolName = null;
        $scope.symbolPrice = null;
        this.select = function (value) {
            $scope.symbolName = value;
            $scope.symbolPrice = marketCtrl.symbolList[value];
        }

        $scope.symbols = [
           { 'name': 'IRANKH' },
           { 'name': 'SAIPA' },
           { 'name': 'BMW' },
           { 'name': 'BENZ' }
        ];

        this.symbolList = {
            'IRANKH': { 'price': 20000 },
            'SAIPA': { 'price': 100000 },
            'BMW': { 'price': 900000 },
            'BENZ': { 'price': 800000 }
        };


        this.getDataSymbol = function () {
            $http({
                method: 'GET',
                url: 'getinstrument'

            }).success(function (data, status, headers, config) {
                if (typeof data !== 'string') {
                    dataSymbol = data;
                    $scope.symbolsQ = dataSymbol;
                    alert('SUCCESS');
                }
                else
                    $scope.notification = data;


            }).error(function (data, status, headers, config) {
                alert('Error:' + data);
            });
        }


        this.updateSymbols = function () {
            alert('not implemented');
        }
//        $interval(function () {
//            $scope.init();
//            $scope.userInst();
//        }, 1000 * 15);


        $scope.userRequests = [];

        this.createRequest = function (price, quantity, type, buyOrSell) {

            $scope.userRequests.push({ 'id': $scope.enteredID, 'instrument': $scope.symbolName, 'price': price, 'quantity': quantity, 'type': type, 'buyOrSell': buyOrSell });
            var len = $scope.userRequests.length - 1;

            alert("SUCCESS: " + $scope.userRequests[len].id + " " + $scope.userRequests[len].instrument + " " + $scope.userRequests[len].price +
                " " + $scope.userRequests[len].quantity + " " + $scope.userRequests[len].type + " " + $scope.userRequests[len].buyOrSell);
            var cost = (buyOrSell === 'buy') ? -1 * price * quantity : price * quantity;
            $http({
                method: 'GET',
                url: 'transaction',
                params: { 'id': $scope.enteredID, 'instrument': $scope.symbolName, 'price': price, 'quantity': quantity, 'type': type, 'order': buyOrSell }
            }).success(function (data, status, headers, config) {
                console.log((typeof data));
                if (typeof data !== 'string') {
                    $scope.notification = data.Error;
                }
                else {
                    $scope.notification = data;
                    $scope.session.money += cost;
                }

            }).error(function (data, status, headers, config) {
                alert('Error:' + data);
            });
        }

    }]);

    app.controller('ReportController', ['$scope', '$http', function ($scope, $http) {
        $scope.transactions = [];

        $scope.notification = null;
        
        
        this.closeNotificationBar = function () {
            $scope.notification = null;
        }

        $scope.init = function () {
            $http({
                method: 'GET',
                url: 'getreport'

            }).success(function (data, status, headers, config) {
                if (typeof data !== 'string') {
                    $scope.transactions = data;
                    //alert('SUCCESS' + data);
                }
                else
                    $scope.notification = data;

            }).error(function (data, status, headers, config) {
                alert('Error:' + data);
            });
        }

        //$scope.init();

        this.search = function (buyerID, buyerName, buyerFamily, sbuyerBalance, ebuyerBalance, sellerID, sellerName, sellerFamily, ssellerBalance, esellerBalance, instrument, type, startQuantity, endQuantity, startPrice, endPrice, startDate, endDate) {
            $http({
                method: 'GET',
                url: 'search',
                params: {
                    'buyerID': buyerID, 'buyerName': buyerName, 'buyerFamily': buyerFamily, 'sbuyerBalance': sbuyerBalance, 'ebuyerBalance': ebuyerBalance,
                    'sellerID': sellerID, 'sellerName': sellerName, 'sellerFamily': sellerFamily, 'ssellerBalance': ssellerBalance, 'esellerBalance': esellerBalance,
                    'instrument':instrument,'type':type,'startQuantity':startQuantity,'endQuantity':endQuantity,'startPrice':startPrice,
                    'endPrice': endPrice, 'startDate': startDate, 'endDate': endDate
                }
            }).success(function (data, status, headers, config) {
                if (typeof data !== 'string') {
                    $scope.transactions = data;
                    //alert('SUCCESS' + data);
                }
                else
                    $scope.notification = data;

            }).error(function (data, status, headers, config) {
                alert('Error:' + data);
            });
        }

    }]);
    
    app.controller('AddNewSymbolCtrl',['$scope', '$http', function ($scope, $http){
        this.addNewSymbol = function (symbol,amount,price){
            //alert(symbol+'-'+amount+'-'+price);
            $http({
                method: 'GET',
                url: 'addnewsymbol',
                params: { 'symbol': symbol,'amount': amount, 'price': price }
            }).success(function (data, status, headers, config) {
                alert('Success: '+data);
            }).error(function (data, status, headers, config) {
                alert('Error:' + data);
            });
        }
    }]);

    app.controller('DepositCtrl',['$scope', '$http', function ($scope, $http){
        this.sendReq = function (amount){
            alert('amount:'+amount);
            $http({
                method: 'GET',
                url: 'deposit',
                params: { 'id': 'ham','amount': amount }
            }).success(function (data, status, headers, config) {
                alert('Success: '+data);
            }).error(function (data, status, headers, config) {
                alert('Error:' + data);
            });
        }
    }]);

    app.controller('TransacLimitCtrl',['$scope', '$http', function ($scope, $http){
        this.setLimit = function (amount){
            alert('amount:'+amount);
            $http({
                method: 'GET',
                url: 'transaclimit',
                params: { 'id': '1','amount': amount }
            }).success(function (data, status, headers, config) {
                alert('Success: '+data);
            }).error(function (data, status, headers, config) {
                alert('Error:' + data);
            });
        }
    }]);

    app.controller('AppNewSymbolCtrl',['$scope', '$http', function ($scope, $http){
        $scope.symbols=[{'name':'BMW','quantity':200,'price':355}];
        this.setLimit = function (symbol,but){
            alert('amount:'+symbol+'but: '+but);
            $http({
                method: 'GET',
                url: 'appnewsymbol',
                params: { 'id': '1','symbol': symbol.name, 'price':symbol.price,'amount':symbol.quantity,'cmd':but }
            }).success(function (data, status, headers, config) {
                alert('Success: '+data);
            }).error(function (data, status, headers, config) {
                alert('Error:' + data);
            });
        }
    }]);

})();
