//"use strict";
use(function () {

	var JCR = sling.getService(Packages.it.cgm.osgi.srv.myjcr.HelloService);
	
	return {
		result : JCR.test("createnode")
	}
});