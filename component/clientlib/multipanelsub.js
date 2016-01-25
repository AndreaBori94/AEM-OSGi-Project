CQ.Ext.ns("ExperienceAEM");

ExperienceAEM.MultiFieldPanel = CQ.Ext.extend(CQ.Ext.Panel, {
    panelValue: '',

    constructor: function(config){
        config = config || {};
        ExperienceAEM.MultiFieldPanel.superclass.constructor.call(this, config);
    },

    initComponent: function () {
        ExperienceAEM.MultiFieldPanel.superclass.initComponent.call(this);

        this.panelValue = new CQ.Ext.form.Hidden({
            name: this.name
        });

        this.add(this.panelValue);

        var dialog = this.findParentByType('dialog');

        dialog.on('beforesubmit', function(){
            var value = this.getValue();

            if(value){
                this.panelValue.setValue(value);
            }
        },this);
    },

    getValue: function () {
        var pData = {};

        this.items.each(function(i){
            if(i.xtype == "labelSkill" || i.xtype == "hidden" || !i.hasOwnProperty("cName")){
                return;
            }

            pData[i.cName] = i.getValue();
        });

        return $.isEmptyObject(pData) ? "" : JSON.stringify(pData);
    },

    setValue: function (value) {
        this.panelValue.setValue(value);

        var pData = JSON.parse(value);

        this.items.each(function(i){
            if(i.xtype == "labelSkill" || i.xtype == "hidden" || !i.hasOwnProperty("cName")){
                return;
            }

            if(!pData[i.cName]){
                return;
            }

            i.setValue(pData[i.cName]);
        });
    },

    validate: function(){
        return true;
    },

    getName: function(){
        return this.name;
    }
});

CQ.Ext.reg("multifield-panel-sub", ExperienceAEM.MultiFieldPanel);