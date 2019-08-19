import datetime

from common.baseObject import HLY
from config import api_urls
from common.apiRequest import ApiRequest
from common.parameter import  GetConfigp
from common.log import logger
import time
from common.globalMap import GlobalMap
from config.api_urls import query_loan_url, expenseTypeOID, formType, subsidy_rule

api = ApiRequest()
glo = GlobalMap()
pa = GetConfigp('./config/hly.config')
hly=HLY()
def get_account():
    """
    获取登录信息
    :return:
    """
    code, r_json = api.response_json(api_urls.account, 'get',header=hly.apilogin_agin())
    if code == 200:
        return r_json
    else:
        print("获取登录信息失败")


def get_company():
    """
    获取公司的信息
    :return:
    """
    code,response =api.response_json(api_urls.company,"get",header=hly.apilogin_agin())
    if code ==200:
        return response
    else:
        print("获取公司的信息失败")

def get_form(formType):
    """
    获取表单信息
    :return:
    """
    data={"formType":formType}
    code, r_json = api.response_json(api_urls.get_form_url, 'get',rdata=data,header=hly.apilogin_agin())
    if code == 200:
        import  json
        # logger.info(json.dumps(r_json))
        return r_json
    else:
        logger.info("接口登录失败的信息：%s" %r_json)
        print("获取登录信息失败")


def get_ControlId(formid):
    """
    根据formid获取控件id
    :param formType:
    :return:
    """
    code, r_json = api.response_json(api_urls.get_ControlId+formid, 'get', header=hly.apilogin_agin())
    if code == 200:
        return r_json
    else:
        print("获取登录信息失败")
def get_expenseReportOID():
    """
     单据审核页面查看报销单
    :return:
    """
    data={"applicantOID":None,
          "businessCode":None,
          "corporationOIDs":[],
          "endDate":None,
          "startDate":None,
          "departmentOids":[],
          "minTotalAmount":None,
          "maxTotalAmount":None,
          "status":"prending_audit"}
    code, r_json = api.response_json(api_urls.get_check,"post",header=hly.apilogin_agin(),rjson=data)
    return r_json[0]["expenseReportOID"]
def Finance_Search_reimbursement(business_Code):
    """
    财务管理-报销单查-搜索报销单
    :param business_Code 报销单ID:
    :return:
    """
    import time
    todayBegin=time.strftime('%Y-%m-%d',time.localtime(time.time()))+' 00:00:00'
    todayEnd = time.strftime('%Y-%m-%d', time.localtime(time.time())) + ' 23:59:59'
    data={"businessCode":business_Code,"beginDate":todayBegin,"endDate":todayEnd,"searchCorporations":[],"entityType":1002}
    # data={"businessCode":"ER00876408","beginDate":"2019-01-16 00:00:00","endDate":"2019-02-16 23:59:59","searchCorporations":[],"entityType":1002}
    # logger.info(data)
    code, r_json = api.response_json(api_urls.Finance_reimbursement_Find, "post", header=hly.apilogin_agin(), rjson=data)
    # logger.info(json.dumps(r_json))
    return r_json[0]["entityOID"]


def Finance_reimbursementView_print(entityOID):
    """
    财务管理-报销单查看-打印报销单
    :param entityOID:
    :return:
    """
    code, r_json = api.response_json(api_urls.Finance_reimbursement_print+entityOID, 'get',rdata=None,header=hly.apilogin_agin())
    if code == 200:
        return r_json
    else:
        print("获取登录信息失败")

def expense_yangzhao():
    date =time.strftime("%Y-%m-%d",time.localtime())
    times =(datetime.datetime.now()+datetime.timedelta(minutes=30)).strftime("%H:%M:%S")
    expense_typeOID =["f9b874ae-946d-4a86-b4ea-81331f39097a","a8ff1ce0-1a64-43f1-8ee4-9e0fa63b1a4f","a8ff1ce0-1a64-43f1-8ee4-9e0fa63b1a4f"]
    username=glo.get("WebUsername")
    if username =="17960000000":
        expenseTypeOID =expense_typeOID[0]
    if username == "13609265517":
        expenseTypeOID =expense_typeOID[1]
    if username == "17709280742":
        expenseTypeOID =expense_typeOID[2]
    data={
	"attachments": [],
	"withReceipt": False,
	"invoiceStatus": "INIT",
	"invoiceCurrencyCode": "CNY",
	"comment": "扬招费用",
	"expenseTypeOID": "%s"% expenseTypeOID,
	"amount": 20,
	"createdDate": "%s"%date,
	"createLocation": "{\"latitude\":34.210496,\"longitude\":108.840391,\"address\":\"\"}",
	"data": [{
		"fieldType": "DATETIME",
		"name": "出发时间",
		"value": "%sT%sZ"%(date,times),
		"messageKey": "start.time",
		"sequence": 4,
		"customEnumerationDTO": None
	}, {
		"fieldType": "GPS",
		"name": "出发地",
		"value": "{\"latitude\":34.210496,\"longitude\":108.840391,\"address\":\"陕西省西安市雁塔区鱼化寨街道西安环普科技产业园G4座环普科技产业园2期\",\"time\":\"2019-03-18T15:30:57Z\",\"timeDsp\":\"2019-03-18 15:30:57\"}",
		"messageKey": "departure.location",
		"sequence": 2,
		"customEnumerationDTO": None
	}, {
		"fieldType": "GPS",
		"name": "目的地",
		"value": "{\"latitude\":34.210496,\"longitude\":108.840391,\"address\":\"陕西省西安市雁塔区鱼化寨街道西安环普科技产业园G4座环普科技产业园2期\",\"time\":\"2019-03-18T15:30:59Z\",\"timeDsp\":\"2019-03-18 15:30:59\"}",
		"messageKey": "destination.location",
		"sequence": 3,
		"customEnumerationDTO": None
	}, {
		"fieldType": "DATETIME",
		"name": "到达时间",
		"value": "%sT%sZ"%(date,times),
		"messageKey": "end.time",
		"sequence": 5,
		"customEnumerationDTO": None
	}, {
		"fieldType": "TEXT",
		"name": "城市",
		"value": "",
		"messageKey": "city",
		"sequence": 1,
		"customEnumerationDTO":None
	}]
}
    logger.info("date:%s"%data)
    code, json = api.response_json(api_urls.yangzhao_url,"post", header=hly.apilogin_agin(), rjson=data)
    logger.info("扬招费用:%s,%s"%(code, json))
    return code


def query_loan(loan_code):
    """
    查询借款单的接口
    :param loan_code:
    :return:
    """
    date = time.strftime("%Y-%m-%d", time.localtime())
    body ={
    "businessCode":"%s"%loan_code,
    "submittedDateEnd":"%sT15:59:59.000Z"%date,
    "submittedDateStart":"2019-08-01T16:00:00.000Z",
    "departmentOids":[
    ],
    "payeeType":None,
    "payeeId":None,
    "page":0,
    "size":10,
    "sortDTOs":[
    ]
}
    code, json = api.response_json(query_loan_url, "post", header=hly.apilogin_agin(), rjson=body)
    if code ==200:
        logger.info(json)
        return json["rows"][0]["id"]
    else:
        logger.info("查询借款单报错：%s,%s"%(code,json))

def confirm(id,loan_number):
    """
    确认付款
    :param id:
    :return:
    """
    date = time.strftime("%Y-%m-%d", time.localtime())
    body={
    "currentPageIds":[
        "%s" % id
    ],
    "entityType":3001,
    "excludeIds":[

    ],
    "modelEnum":"CURRENT_PAGE",
    "submittedDateStart":"2019-01-07T16:00:00.000Z",
    "submittedDateEnd":"%sT15:59:59.000Z" % date,
    "businessCode":"%s" % loan_number,
    "comment": None,
    "corporationOids":[

    ],
    "formOids":[

    ],
    "applicantOid":None,
    "companyIds":[

    ],
    "payeeType":None,
    "payeeId":None
}
    code, json = api.response_json(api_urls.confirm, "post", header=hly.apilogin_agin(), rjson=body)
    if code ==200:
        logger.info("确认付款成功")
    else:
        logger.info("确认付款失败：%s,%s"%(code,json))

def confirm_paid(id):
    """
    确认已付款
    :return:
    """
    date = time.strftime("%Y-%m-%d", time.localtime())
    body ={
    "currentPageIds":[
        "%s"%id
    ],
    "entityType":3001,
    "excludeIds":[

    ],
    "modelEnum":"CURRENT_PAGE",
    "submittedDateStart":"%sT16:00:00.000Z"%date,
    "submittedDateEnd":"%sT15:59:59.000Z"%date,
    "businessCode":None,
    "comment":None,
    "corporationOids":[

    ],
    "formOids":[

    ],
    "applicantOid":None,
    "companyIds":[

    ],
    "payeeType":None,
    "payeeId":None
}
    code, json = api.response_json(api_urls.confirm_paid, "post", header=hly.apilogin_agin(), rjson=body)
    if code == 200:
        logger.info("确认已付款成功")
    else:
        logger.info("确认已付款失败：%s,%s" % (code, json))

def get_expenseTypeOID():
    """
    获取费用类型的OID
    :return:
    """
    setOfBooksId = get_company()["setOfBooksId"]
    requestUrl = expenseTypeOID%setOfBooksId
    code, r_json = api.response_json(requestUrl, 'get', header=hly.apilogin_agin())
    # logger.info("费用类型的OID:%s" % r_json)
    if code == 200:
        return r_json
    else:
        logger.info("获取费用类型id失败")


def Consumer_expense(amount, expenseNmae):
    """
    第三方的费用
    :return:
    """
    utctime = datetime.datetime.now().isoformat()
    if glo.get("env") == "console":
        expenseTypeOID = get_expenseTypeOID()["rows"][0]["expenseTypes"]
    else:
        expenseTypeOID = get_expenseTypeOID()["rows"][1]["expenseTypes"]
    # logger.info("xixiixiixiix:%s"%expenseTypeOID)
    time.sleep(2)
    for i in expenseTypeOID:
        if i["name"] == "%s"%expenseNmae:
            OID = i["expenseTypeOID"]

    logger.info("hahahahahah:%s" % OID)
    userOID =get_account()["userOID"]
    body ={
  "expenseTypeOID": "%s" % OID,
  "createdDate": "%s" % utctime,
  "currencyCode": "CNY",
  "invoiceStatus": "INIT",
  "withReceipt": True,
  "applicationNumber":None,
  "userOID": "%s" % userOID,
  "amount": amount,
  "paymentType": 1001,
  "readonly": True,
  "data": [
  ]
}
    code, json = api.response_json(api_urls.third_expense, "post", header=hly.apilogin_agin(), rjson=body)
    if code == 201:
        logger.info("费用推送完成")
    else:
        logger.info("费用推送失败：%s,%s" % (code, json))

def get_jobID():
    """
    获取员工的jobId
    :return:
    """
    code, json = api.response_json(api_urls.account, "get", header=hly.apilogin_agin(), rdata=None)
    if code == 200:
        return json["companyWithUserJobsDTOList"][0]["userJobsDTOList"][0]["id"]

def open_auto_route_Calculation():
    """
    差补界面打开自动计算行程
    :return:
    """
    body = {"allowanceReduplicateTreatment": 1001,
           "allowanceAmountModify": 1002,
           "allowanceAttachExpenseReportDisable": "true",
           "travelAutoCalculateEnable": True
           }
    code, json = api.response_json(api_urls.travel_setting, "post", header=hly.apilogin_agin(), rjson=body)
    time.sleep(2)
    logger.info("差补规则基础设置:%s" % json)

def close_auto_route_Calculation(allowanceAttachExpenseReportDisable ="true", travelAutoCalculateEnable =False):
    """
    差补界面关闭自动计算行程
    :return:
    """
    body ={"allowanceReduplicateTreatment": 1001,
           "allowanceAmountModify": 1002,
           "allowanceAttachExpenseReportDisable": allowanceAttachExpenseReportDisable,
           "travelAutoCalculateEnable": travelAutoCalculateEnable
           }
    code, json = api.response_json(api_urls.travel_setting, "post", header=hly.apilogin_agin(), rjson=body)
    time.sleep(2)
    logger.info("差补规则基础设置:%s"% json)


def get_userOID():
    """
    获取用户的userOID
    :return:
    """
    return get_account()["userOID"]


def get_formType(formName):
    """
    :param formName:  表单的名称
    :return:
    """
    userOID = get_userOID()
    code, json = api.response_json(formType % userOID, "get", header=hly.apilogin_agin(), rdata=None)
    if code == 200:
        for i in json:
            if i["formName"] == formName:
                return i["formOID"]
            pass

def change_subsidy_rule(formName, ALLOWANCE_CITY = True):
    """
    修改差补规则
    :param ALLOWANCE_CITY: 为True时表示差补选择城市,为false表示城市为空
    :return:
    """
    body = {
    "formOID":"%s" % get_formType(formName),
    "formName":"%s"% formName,
    "dimensionTypes":[
        {
            "name":"DEFAULT_RULES",
            "code":1001,
            "selectable":False,
            "dimensionDetails":[
                {
                    "name":"EXPENSE_LEVEL",
                    "code":"EXPENSE_LEVEL",
                    "selected":True
                },
                {
                    "name":"USER_GROUPS",
                    "code":"USER_GROUPS",
                    "selected":True
                },
                {
                    "name":"CURRENCY_CODE",
                    "code":"CURRENCY_CODE",
                    "selected":True
                },
                {
                    "name":"ALLOWANCE_AMOUNT",
                    "code":"ALLOWANCE_AMOUNT",
                    "selected":True
                },
                {
                    "name":"ALLOWANCE_CITY",
                    "code":"ALLOWANCE_CITY",
                    "selected":ALLOWANCE_CITY
                }
            ],
            "formFieldDetails":None,
            "itineraryFieldList":None
        },
        {
            "name":"TRAVEL_FIELDS",
            "code":1002,
            "selectable":True,
            "dimensionDetails":[
                {
                    "name":"TRAVEL_HOURS",
                    "code":"TRAVEL_HOURS",
                    "selected":False
                },
                {
                    "name":"TRAVEL_DAYS",
                    "code":"TRAVEL_DAYS",
                    "selected":False
                }
            ],
            "formFieldDetails":[

            ],
            "itineraryFieldList":None
        },
        {
            "name":"SPECIAL_RULES",
            "code":1003,
            "selectable":True,
            "dimensionDetails":[
                {
                    "name":"HOLIDAY_WEEKEND",
                    "code":"HOLIDAY_WEEKEND",
                    "selected":False
                },
                {
                    "name":"DEPARTURE_DAY",
                    "code":"DEPARTURE_DAY",
                    "selected":False
                },
                {
                    "name":"RETURN_DAY",
                    "code":"RETURN_DAY",
                    "selected":False
                }
            ],
            "formFieldDetails":None,
            "itineraryFieldList":None
        },
        {
            "name":"ITINERARY_FIELDS",
            "code":1004,
            "selectable":True,
            "dimensionDetails":[
            ],
            "formFieldDetails":None,
            "itineraryFieldList":None
        }
    ],
    "tips":"",
    "withTips":False,
    "subsidySelection":True,
    "applicationAutoSubsidy":False
}
    code, json = api.response_json(subsidy_rule, "post", header=hly.apilogin_agin(), rjson=body)
    logger.info("修改的差补规则：%s"%json)








