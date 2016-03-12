(function() {
var rootUrl = "http://carshare.fexcosoftware.com";
// Localize jQuery variable
var jQuery;

/******** Load jQuery if not present *********/
if (window.jQuery === undefined || window.jQuery.fn.jquery !== '1.4.2') {
    var script_tag = document.createElement('script');
    script_tag.setAttribute("type","text/javascript");
    script_tag.setAttribute("src",
        "http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js");
    if (script_tag.readyState) {
      script_tag.onreadystatechange = function () { // For old versions of IE
          if (this.readyState == 'complete' || this.readyState == 'loaded') {
              scriptLoadHandler();
          }
      };
    } else {
      script_tag.onload = scriptLoadHandler;
    }
    // Try to find the head, otherwise default to the documentElement
    (document.getElementsByTagName("head")[0] || document.documentElement).appendChild(script_tag);
} else {
    // The jQuery version on the window is the one we want to use
    jQuery = window.jQuery;
    main();
}

/******** Called once jQuery has loaded ******/
function scriptLoadHandler() {
    // Restore $ and window.jQuery to their previous values and store the
    // new jQuery in our local jQuery variable
    jQuery = window.jQuery.noConflict(true);
    // Call our main function
    main(); 
}

/******** Our main function ********/
function main() { 
    jQuery(document).ready(function($) { 
        /******* Load CSS *******/
        // var css_link = $("<link>", { 
        //     rel: "stylesheet", 
        //     type: "text/css", 
        //     href: "style.css" 
        // });
        // css_link.appendTo('head');          

        /******* Load HTML *******/
        var journeys_url = rootUrl+"/api/journey/allJourneys"
        $.getJSON(journeys_url, function(data) {
          $('#carShareWidget').html(getHTMLText(data));
        });
    });
}

function convertDate(inputFormat) {
  function pad(s) { return (s < 10) ? '0' + s : s; }
  var d = new Date(inputFormat);
  return [pad(d.getDate()), pad(d.getMonth()+1), d.getFullYear()].join('/');
}

var getHTMLText = function(data){
    var textToReturn = ""+
    "<div class='carShareWidgetContainer reset-this'>"+
        "<div class='carShareWidgetHeading'>"+
            "<a class='carShareWidgetHeadingText' align='center' target='_blank' href='"+rootUrl+"'>"+
                "<h2 class='carShareWidgetName'>Car Share</h2>"+
            "</a>"+
            "<h4 class='carShareWidgetTagline'>A free car-pooling application</h4>"+
        "</div>";
    var journeys = data.content;
    for(var i = 0; i < journeys.length;i++){
        var journeyText = ""+
        "<div class='carShareWidgetJourneyHead'>"+
            "<a class='carShareWidgetLinkToJourney' target='_blank' href='"+rootUrl+"/#/findJourneys//journey/"+journeys[i].id+"'>"+journeys[i].source+" &#8680 "+journeys[i].destination+"</a>"+
        "</div>"+
        "<div class='carShareWidgetJourneyBody'>"+
            "<table style='min-height:64px'>"+
                "<tr>"+
                    "<td class='carShareWidgetColLeft'>"+
                        "<div>"+
                            "<a target='_blank' href='"+rootUrl+"/#/profile/"+journeys[i].username+"'><img class='carShareWidgetProfilePic' src='"+rootUrl+"/user/profile/getProfilePicture/"+journeys[i].username+"' /></a>"+
                        "</div>"+
                    "</td>"+
                    "<td id='carshareWidgetElement' class='carShareWidgetColCenter'>"+
                        "<p id='carshareWidgetElement' class='carShareWidgetAmount'><b>&#8364;"+journeys[i].cost+"</b></p>"+
                        "<p id='carshareWidgetElement' class='carShareWidgetDate'><b>"+convertDate(new Date(journeys[i].date))+"</b></p>"+
                    "</td>"+
                    "<td class='carShareWidgetBodyRight'>"+
                            "<a class='carShareWidgetJourneyButton' target='_blank' href='"+rootUrl+"/#/findJourneys//journey/"+journeys[i].id+"'><h2 class='carShareWidgetButton'>&#8680</h2></a>"+
                    "</td>"+
                "</tr>"+
            "</table>"+
        "</div>";
        textToReturn += journeyText;
    }
    textToReturn+="</div>";
    return textToReturn;
};

})(); // We call our anonymous function immediately