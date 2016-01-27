var dialog = {
    Cerchi : {
        0 : { type: "Lega 14", price : 150 },
        1 : { type: "Lega 16", price: 180 },
    },
    Motore : {
        0 : { type: "2000 GPL", price: 14 },
        1 : { type: "2100 Diesel", price: 14 },
        2 : { type: "2200 Benzina", price: 14 },
    },
    Colore : {
        0 : { type: "Nero", price: 14 },
        2 : { type: "Rosso", price: 14 }, 
        3 : { type: "Bianco", price: 14 }, 
    },
    Frontalino : {
        0 : { type: "Autoradio", price: 300 }, 
        1 : { type: "GPS Navigatore", price: 700 }, 
        2 : { type: "Autoradio + GPS", price: 900 }  
    },
    Volante : {
        0 : { type: "Pelle", price: 300 }, 
        1 : { type: "Plastica", price: 700 },   
    }
};

$(document).ready(function(){
    
    //crea variabile profilo dato il dialog
    var profile = {};
    for ( i in dialog ) {
        profile[i] = ""; 
    } 
    
    //creazione view
    var view = "";
    view += "<div id=\"app_nav\" class=\"btn-group btn-group-justified\" role=\"group\">";
    view += "   <div class=\"btn-group\" role=\"group\">";
    view += "       <button id=\"btn_cfg_prof\" type=\"button\" class=\"btn btn-default\">Configurazione Profilo</button>";
    view += "   </div>";
    view += "   <div class=\"btn-group\" role=\"group\">";
    view += "       <button id=\"btn_viw_prof\" type=\"button\" class=\"btn btn-default\">Visualizza Profilo</button>";
    view += "   </div>";
    view += "</div>";
    $('#application_view').append(view);
    
    var current_view = 0;
    
    $('#btn_cfg_prof').click(function(){
        current_view = 0;
        $('#pnl_viw').hide();
        $('#pnl_cfg').show();
    });
    $('#btn_viw_prof').click(function(){
        current_view = 1;
        $('#pnl_viw').show();
        $('#pnl_cfg').hide();
        // update_view
        var print = "";
        for ( i in profile ) {
            print += '<a href="#" class="list-group-item">'+i+" => "+profile[i]+'</a>';
        }
        $('#profList').html(print);
    });
    
    //creazione pannello CREA
    var panels = "";
    panels += "<div id=\"pnl_cfg\" class=\"panel panel-default\">";
    panels += "    <div class=\"panel-heading\">";
    panels += "        <h3 class=\"panel-title\">Configurazione Profilo</h3>";
    panels += "    </div>";
    panels += "    <div id=\"pnl_cfg_content\" class=\"panel-body\">";
    panels += "    </div>";
    panels += "         <div id=\"car_gen_action\"></div>";
    panels += "</div>";
    $('#application_view').append(panels);
    
    //creazione pannello VEDI
    var panels = "";
    // Destra
    panels += "<div id=\"pnl_viw\"class=\"panel panel-default\">";
    panels += "    <div class=\"panel-heading\">";
    panels += "        <h3 class=\"panel-title\">Visualizza Profilo</h3>";
    panels += "    </div>";
    panels += "    <div id=\"pnl_viw_content\" class=\"panel-body\">"; 
    panels += '<div class="row">';
    panels += "<div class='col-xs-4 col-sm-4 col-md-4 col-lg-4'>";
    panels += '<div class="list-group"><a href="#" class="list-group-item active">Audi R8</a>';
    panels += '<a href="#" class="list-group-item">Audi TT</a>';
    panels += '<a href="#" class="list-group-item">Audi TTS</a>';
    panels += '<a href="#" class="list-group-item">Audi Sport</a>';
    panels += '<a href="#" class="list-group-item">Non me ne vengono in mente altre</a>';
    panels += '</div>';
    
    panels += "    </div>";
    panels += "<div class='col-xs-8 col-sm-8 col-md-8 col-lg-8'>";
    //VIEW GOES HERE
        panels += '<div class="list-group" id="profList"></div>';    
    //VIEW GOES HERE
    panels += "</div>";
    panels += "</div>";
    panels += "</div>";
    // Sinistra
    $('#application_view').append(panels);
    
    
    var output = "";
    output += "<nav class=\"navbar navbar-default\">";
    output += "<div class=\"container-fluid\">";
    output += "<div class=\"navbar-header\">";
    output += "<a class=\"navbar-brand\" href=\"#\">AUDI R8</a>";
    output += "</div>";
    output += "<div class=\"collapse navbar-collapse\">";
    output += "<ul class=\"nav navbar-nav\">";
    
    
    output += "<li class=\"dropdown\">";
    output += "<a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">";
    output += "    Profilo <span class=\"caret\"></span>";
    output += "</a>";
    output += "<ul class=\"dropdown-menu\">";
    output += "<li class='elem'><a href=\"#\"><label>Nome Veic<input type='text' /></label></a></li>";
    output += "<li class='elem'><a href=\"#\"><label>Creato Da<input type='text' /></label></a></li>";
    output += "</ul>";
    output += "</li>";
    
    for ( cat in dialog )
    {
    output += "<li class=\"dropdown\">";
    output += "<a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">";
    output += "    "+cat+" <span class=\"caret\"></span>";
    output += "</a>";
    output += "<ul class=\"dropdown-menu\">";
        for ( elm in dialog[cat] ) {        
            output += "<li class='elem'><a href=\"#\">"+cat+" | "+dialog[cat][elm].type+" | "+dialog[cat][elm].price+"</a></li>";
        }
    output += "</ul>";
    output += "</li>";
    }
    
    output += "</ul>";
    output += "<ul class=\"nav navbar-nav navbar-right\">";
    output += "<li><a href=\"#\">Salva</a></li>";
    output += "</ul>";
    output += "</div>";
    output += "</div>";
    output += "</nav>";
    $('#pnl_cfg_content').append(output);
        
    
    $('.elem').click(function(){
        
        var info = ( $(this).text() );
        var cat = info.split("|")[0].trim();
        var name = info.split("|")[1].trim();
        var value = info.split("|")[2].trim();        
        console.log( cat + " " + name + " " + value);
        
        profile[cat] = name;
        console.log(profile);
    });
    
    //init
    $('#pnl_viw').hide();
});