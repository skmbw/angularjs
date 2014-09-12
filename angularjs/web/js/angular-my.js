var projectName = '/' + window.location.pathname.split('/')[1];
var userUrl = {
    'addUrl' : projectName + '/user/add',
    'deleteUrl' : projectName + '/user/delete',
    'updateUrl' : projectName + '/user/update',
    'queryUrl' : projectName + '/user/userList'
};
var user = angular.module('userService', [ 'ngResource' ], angular.noop);
user.controller('userController', function($scope, $resource) {
    var actions = {
        'add' : {
            method : 'PUT',
            isArray : true,
            headers : {
                'Content-Type' : 'application/json'
            }
        },
        'deletes' : {//delete是js的关键字
            method : 'DELETE',
            isArray : true
        },
        'query' : {
            method : 'GET',
            isArray : true
        },
        'update' : {
            method : 'POST',
            isArray : true,
            headers : {
                'Content-Type' : 'application/json'
            }
        }
    };
    var getUserList = $resource(userUrl.queryUrl, {
        page : 1,
        pageSize : 20
    }, actions.query);
    getUserList.query({}, function(data) {// 这个会自动执行的
        subobj = data;
        $scope.mydata = data;
    });

    var userAdd = $resource(userUrl.addUrl, {
        page : 1,
        pageSize : 20
    }, actions);
    $scope.addUserClick = function() {// 类似这种，点击了才执行
        userAdd.add($scope.saveUser, function(data) {
            //subobj = data;
            $scope.mydata = data;
        });
    };

    var userUpdate = $resource(userUrl.updateUrl, {
        page : 1,
        pageSize : 20
    }, actions);
    $scope.updateUserClick = function() {// 实际的更新操作
        userUpdate.update($scope.modifyUser, function(data) {
            subobj = data;
            $scope.mydata = data;
        });
    };

    var userDelete = $resource(userUrl.deleteUrl, {
        page : 1,
        pageSize : 20,
        id : ':id'
    }, actions);
    $scope.deleteUser = function(user) {//删除功能
        userDelete.deletes({//userDelete['deletes']也可以
            id : user.id
        }, {}, function(data) {
            subobj = data;
            $scope.mydata = data;
        });
    };
    $scope.updateUser = function(user) {// 对应列表中的更新功能，将数据添加到更新区域
        $scope.modifyUser = user;
    };
});