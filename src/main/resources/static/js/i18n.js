// Trilingual translation dictionary (English, Hindi, Marathi)
const translations = {
    en: {
        appTitle: "Swachh Sathi",
        appTagline: "Smart Waste Management & Civic Sanitation Platform",
        selectRole: "Select Your Role",
        citizen: "Citizen",
        worker: "Sanitation Worker",
        admin: "Municipal Admin",
        citizenDesc: "Report waste spots, track complaints & learn waste segregation.",
        workerDesc: "View assigned pickups, get optimized route & submit resolution proof.",
        adminDesc: "Citywide dashboard, assign tasks, verify resolutions & manage staff.",
        login: "Sign In",
        register: "Create Account",
        logout: "Sign Out",
        email: "Email Address",
        password: "Password",
        fullName: "Full Name",
        phone: "Phone Number",
        address: "Address / Ward",
        reportIssue: "Report Waste Spot",
        wasteType: "Waste Type",
        generalWaste: "General Waste",
        hazardousWaste: "Hazardous Waste",
        constructionWaste: "Construction Debris",
        otherWaste: "Other Waste",
        description: "Description",
        uploadPhoto: "Upload Photo",
        location: "Location",
        clickMapLocation: "Click on map to set location or use current GPS",
        submitReport: "Submit Complaint",
        myComplaints: "My Complaints",
        activeTasks: "Assigned Pickup Tasks",
        optimizeRoute: "Optimize Pickup Route",
        totalStops: "Total Stops",
        estDistance: "Estimated Distance",
        submitProof: "Submit Resolution Proof",
        notes: "Resolution Notes",
        verifyQueue: "Verification Queue",
        approve: "Approve Resolution",
        reject: "Reject / Reassign",
        adminMetrics: "Civic Health Analytics",
        totalComplaints: "Total Complaints",
        resolvedComplaints: "Resolved",
        pendingVerification: "Pending Verification",
        avgTurnaround: "Avg Turnaround",
        days: "days",
        jobApplications: "Worker Applications",
        applyWorker: "Apply as Sanitation Worker",
        experience: "Years of Experience",
        submitApplication: "Submit Application",
        eduSection: "Waste Segregation & Composting Guide",
        helpline: "PMC Citizen Helpline: 1800-1030-222 | Emergency: 112",
        proposalsLink: "View Policy Proposals & SIH Roadmap"
    },
    hi: {
        appTitle: "स्वच्छ साथी",
        appTagline: "स्मार्ट कचरा प्रबंधन एवं नागरिक स्वच्छता मंच",
        selectRole: "अपनी भूमिका चुनें",
        citizen: "नागरिक",
        worker: "सफाई मित्र (कर्मचारी)",
        admin: "नगरपालिका प्रशासक",
        citizenDesc: "कचरे की रिपोर्ट करें, स्थिति ट्रैक करें और अपशिष्ट वर्गीकरण सीखें।",
        workerDesc: "सौंपे गए कार्य देखें, सबसे छोटा मार्ग पाएं और समाधान प्रमाण भेजें।",
        adminDesc: "शहर का डैशबोर्ड, कार्य सौंपना, सत्यापन और कर्मचारी प्रबंधन।",
        login: "लॉग इन करें",
        register: "नया खाता बनाएं",
        logout: "लॉग आउट",
        email: "ईमेल पता",
        password: "पासवर्ड",
        fullName: "पूरा नाम",
        phone: "फ़ोन नंबर",
        address: "पता / वार्ड",
        reportIssue: "कचरे की शिकायत दर्ज करें",
        wasteType: "कचरे का प्रकार",
        generalWaste: "सामान्य कचरा",
        hazardousWaste: "खतरनाक अपशिष्ट",
        constructionWaste: "निर्माण मलबा",
        otherWaste: "अन्य कचरा",
        description: "विवरण",
        uploadPhoto: "फोटो अपलोड करें",
        location: "स्थान",
        clickMapLocation: "स्थान चुनने के लिए मानचित्र पर क्लिक करें",
        submitReport: "शिकायत सबमिट करें",
        myComplaints: "मेरी शिकायतें",
        activeTasks: "सौंपे गए कचरा उठाव कार्य",
        optimizeRoute: "अनुकूलित मार्ग (रूट) बनाएं",
        totalStops: "कुल स्टॉप",
        estDistance: "अनुमानित दूरी",
        submitProof: "समाधान का प्रमाण अपलोड करें",
        notes: "समाधान नोट्स",
        verifyQueue: "सत्यापन कतार",
        approve: "सत्यापित एवं स्वीकृत करें",
        reject: "अस्वीकार करें",
        adminMetrics: "स्वच्छता विश्लेषण और आँकड़े",
        totalComplaints: "कुल शिकायतें",
        resolvedComplaints: "हल की गई",
        pendingVerification: "सत्यापन बाकी",
        avgTurnaround: "औसत समाधान समय",
        days: "दिन",
        jobApplications: "सफाई कर्मचारी आवेदन",
        applyWorker: "सफाई मित्र हेतु आवेदन करें",
        experience: "अनुभव (वर्ष)",
        submitApplication: "आवेदन जमा करें",
        eduSection: "कचरा पृथक्करण एवं खाद निर्माण निर्देशिका",
        helpline: "नागरिक हेल्पलाइन: 1800-1030-222 | आपातकालीन: 112",
        proposalsLink: "नीतिगत प्रस्ताव और एसआईएच रोडमैप देखें"
    },
    mr: {
        appTitle: "स्वच्छ साथी",
        appTagline: "स्मार्ट कचरा व्यवस्थापन आणि नागरी स्वच्छता व्यासपीठ",
        selectRole: "आपली भूमिका निवडा",
        citizen: "नागरिक",
        worker: "स्वच्छता दूत (कामगार)",
        admin: "महापालिका प्रशासन",
        citizenDesc: "कचऱ्याची तक्रार नोंदवा, पाठपुरावा करा आणि कचरा वर्गीकरण शिका.",
        workerDesc: "नियुक्त कामे पहा, सर्वात वेगवान मार्ग मिळवा आणि कामाचा पुरावा द्या.",
        adminDesc: "शहरस्तरीय डॅशबोर्ड, कामे वाटप, पडताळणी आणि कर्मचारी व्यवस्थापन.",
        login: "लॉग इन करा",
        register: "नवीन खाते तयार करा",
        logout: "लॉग आउट",
        email: "ईमेल पत्ता",
        password: "पासवर्ड",
        fullName: "पूर्ण नाव",
        phone: "फोन नंबर",
        address: "पत्ता / प्रभाग",
        reportIssue: "कचऱ्याची तक्रार नोंदवा",
        wasteType: "कचऱ्याचा प्रकार",
        generalWaste: "सर्वसाधारण कचरा",
        hazardousWaste: "धोकादायक कचरा",
        constructionWaste: "बांधकाम राडारोडा",
        otherWaste: "इतर कचरा",
        description: "तपशील",
        uploadPhoto: "फोटो अपलोड करा",
        location: "स्थान",
        clickMapLocation: "स्थान निवडण्यासाठी नकाशावर क्लिक करा",
        submitReport: "तक्रार नोंदवा",
        myComplaints: "माझ्या तक्रारी",
        activeTasks: "नियुक्त कचरा उचल कामे",
        optimizeRoute: "कमी अंतराचा मार्ग (रूट) शोधा",
        totalStops: "एकूण थांबे",
        estDistance: "अंदाजे अंतर",
        submitProof: "काम पूर्ण झाल्याचा पुरावा द्या",
        notes: "नोंदी / शेरा",
        verifyQueue: "पडताळणी रांग",
        approve: "मंजूर करा",
        reject: "नाकारा / पुन्हा पाठवा",
        adminMetrics: "नागरी स्वच्छता विश्लेषण",
        totalComplaints: "एकूण तक्रारी",
        resolvedComplaints: "निवारण झालेल्या",
        pendingVerification: "पडताळणी बाकी",
        avgTurnaround: "सरासरी निवारण वेळ",
        days: "दिवस",
        jobApplications: "स्वच्छता दूत अर्ज",
        applyWorker: "स्वच्छता दूत म्हणून अर्ज करा",
        experience: "कामाचा अनुभव (वर्षे)",
        submitApplication: "अर्ज सादर करा",
        eduSection: "कचरा वर्गीकरण व खतनिर्मिती मार्गदर्शक",
        helpline: "मनपा नागरी हेल्पलाइन: 1800-1030-222 | आपत्कालीन: 112",
        proposalsLink: "धोरणात्मक प्रस्ताव आणि एसआयएच आराखडा पहा"
    }
};

let currentLang = localStorage.getItem('swachh_lang') || 'en';

function setLanguage(lang) {
    if (!translations[lang]) lang = 'en';
    currentLang = lang;
    localStorage.setItem('swachh_lang', lang);
    applyTranslations();
}

function t(key) {
    return (translations[currentLang] && translations[currentLang][key]) || 
           (translations['en'] && translations['en'][key]) || key;
}

function applyTranslations() {
    document.querySelectorAll('[data-i18n]').forEach(el => {
        const key = el.getAttribute('data-i18n');
        if (translations[currentLang] && translations[currentLang][key]) {
            el.textContent = translations[currentLang][key];
        }
    });
    document.querySelectorAll('[data-i18n-placeholder]').forEach(el => {
        const key = el.getAttribute('data-i18n-placeholder');
        if (translations[currentLang] && translations[currentLang][key]) {
            el.setAttribute('placeholder', translations[currentLang][key]);
        }
    });
    const langSelect = document.getElementById('langSelect');
    if (langSelect) langSelect.value = currentLang;
}
