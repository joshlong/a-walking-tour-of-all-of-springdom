<%@ page session="false" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html>
<html>
<head>
    <style type="text/css">
        body, html {
            font-family: helvetica, arial, sans-serif;
            font-size: 90%;
        }

        html, body, #mainDiv {
            width: 100%;
            height: 100%;
            border: 0;
            padding: 0;
            margin: 0;
        }
    </style>
    <script type="text/javascript">
        djConfig = {
            parseOnLoad:true
        };
    </script>

    <link rel="stylesheet" type="text/css"
          href="http://ajax.googleapis.com/ajax/libs/dojo/1.6/dijit/themes/soria/soria.css"/>
    <script src="http://ajax.googleapis.com/ajax/libs/dojo/1.6/dojo/dojo.xd.js"></script>
    <script type="text/javascript">
        dojo.require("dijit.layout.BorderContainer");
        dojo.require("dijit.layout.TabContainer");
        dojo.require("dijit.layout.AccordionContainer");
        dojo.require("dijit.form.NumberTextBox");
        dojo.require("dijit.layout.ContentPane");
    </script>
</head>
<body class="soria">
<div dojoType="dijit.layout.BorderContainer" style="width: 100%; height: 100%;">
    <div dojoType="dijit.layout.ContentPane" region="top">
        <span style="font-size:larger;">Spring CRM</span> |


        <label for="cid"> Customer ID: </label>

        <input id="cid" type="text" dojoType="dijit.form.NumberTextBox" name="customerId" value="69"
               required="true" invalidMessage="Invalid customer ID#.">

        <a href="#" id="lookup">Look Up Customer Information </a>

    </div>
    <div dojoType="dijit.layout.AccordionContainer" region="leading">
        <div dojoType="dijit.layout.AccordionPane" title="pane #1">
            accordion pane #1
        </div>
        <div dojoType="dijit.layout.AccordionPane" title="pane #2">
            accordion pane #2
        </div>
        <div dojoType="dijit.layout.AccordionPane" title="pane #3">
            accordion pane #3
        </div>
    </div>
    <div dojoType="dijit.layout.ContentPane" region="center">

        <div id="customerContainer">
            <table>
                <tr>
                    <td>First Name:</td>
                    <td><span id="fn"> </span></td>
                </tr>
                <tr>
                    <td>Last Name:</td>
                    <td><span id="ln"> </span></td>
                </tr>
            </table>

        </div>

    </div>

    <div dojoType="dijit.layout.ContentPane" region="bottom">
        <tiles:insertAttribute name="footer"/>
    </div>
</div>


<script language="javascript" type="text/javascript">

    var customerLookupTopic = '/customerLookup';

    dojo.ready(function (evt) {


        var lookupLink = dojo.byId('lookup');
        var customerId = dojo.byId('cid');

        dojo.subscribe(customerLookupTopic, function (cid) {
            var url = "http://localhost:8080/customer/" + cid;
            dojo.xhrGet({
                url:url,
                headers:{ Accept:'application/json' },
                handleAs:'json',
                load:function (cr) {
                    //var cc = dojo.byId('customerContainer');
                    alert(cr.firstName + ' ' + cr.lastName)
                    var fn = dojo.byId('fn'),
                            ln = dojo.byId('ln');

                    fn.innerHTML = cr.firstName;
                    ln.innerHTML = cr.lastName;


                },
                error:function (e) {
                    alert("that doesn't appear to be a valid customer ID#! Please try again!");
                }
            });

        });
        dojo.connect(customerId, 'change', function (evt) {
            var customerId = parseInt(dojo.byId('cid').value);
            dojo.publish(customerLookupTopic, [ customerId  ]);
        });

        dojo.connect(lookupLink, 'click', function (evt) {
            var customerId = parseInt(dojo.byId('cid').value);
            dojo.publish(customerLookupTopic, [customerId ]);
        });
    })
</script>

</body>
</html>