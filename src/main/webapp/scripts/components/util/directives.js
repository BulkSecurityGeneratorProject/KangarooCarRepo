/**
 * 
 */
angular.module('carshareApp.directive.search',[])
	.directive('mySearch', function () {	
		return {
			restrict: 'E',	        
			templateUrl: 'scripts/components/util/search.html'
	};	
});

angular.module('carshareApp.directive.map', [])
.directive('myMap', function () {	
	 return {
	        restrict: 'E',
	        replace: true,
	        template: '<div></div>',
	        link: function(scope, element, attrs) {
	            var myOptions = {	            		
	                zoom: 6,
	                draggable: true,
	                center: new google.maps.LatLng(53.312800596408394, -7.910156247500026),
	                mapTypeId: google.maps.MapTypeId.ROADMAP,
	                region: "ireland"
	            };		            
	            map = new google.maps.Map(document.getElementById(attrs.id), myOptions);
	        }//Link
	    }; //return 
	    
});