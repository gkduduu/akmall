//
//  SettingTableViewController.m
//  AKMall
//
//  Created by 한병일 on 2014. 6. 20..
//  Copyright (c) 2014년 한병일. All rights reserved.
//

#import "SettingTableViewController.h"

@interface SettingTableViewController ()

@end

@implementation SettingTableViewController
@synthesize arrNotilist;
@synthesize arrNotiItems;
@synthesize arrNotiDenytimeItems;
@synthesize arrNotiSpecialdayItems;
@synthesize activityView;

#define kNotiDeviceUSERRoot_Item         @"root"
#define kNotiDeviceUSER_Item             @"updateduser"
#define kNotiDeviceUSER_Gubun            @"gubun"
#define NOTI_DEVICEUSER_ITEM_ATTREBUTE [[NSArray alloc] initWithObjects:kNotiDeviceUSERRoot_Item, kNotiDeviceUSER_Item, kNotiDeviceUSER_Gubun, nil]


#define kroot_Item          @"root"
#define kNotiset_item       [[NSArray alloc] initWithObjects:@"deny",  @"specialday", @"denytime", nil]
#define kNotiset_items      @"item"
#define kNotisetElements    [[NSDictionary alloc] initWithObjectsAndKeys:[[NSArray alloc] initWithObjects:@"EVENT_NAME", @"EVENT_ID", @"DENY",nil], @"deny", [[NSArray alloc] initWithObjects:@"BIRTHDAY", @"WEDDING_DATE", @"SOLAR_GB", nil], @"specialday", [[NSArray alloc] initWithObjects:@"START_HH", @"END_HH", nil], @"denytime", nil]
#define NOTISET_DETAIL_ATTREBUTE [[NSArray alloc] initWithObjects:kroot_Item, kNotiset_item, kNotiset_items, kNotisetElements, nil]

#define FONT_COLOR_BLACK    [UIColor colorWithWhite:0.200 alpha:1.000]
#define FONT_COLOR_PINK     [UIColor colorWithRed:0.824 green:0.000 blue:0.412 alpha:1.000]

#define MSG_DELEGATE_BTN2(TITLE, CANCEL_BUTTON_TITLE, OK_BUTTON_TITLE, MSG, DELEGATE) [[[UIAlertView alloc] initWithTitle:TITLE message:MSG delegate:DELEGATE cancelButtonTitle:CANCEL_BUTTON_TITLE otherButtonTitles:OK_BUTTON_TITLE, nil] show]
#define kNotiBathMsgGubun @"생일, 결혼기념일 설정 이용하시겠습니까?"


#define kNotidenyupdateRoot_Item         @"root"
#define kNotidenyupdate_Item             @"denyupdate"
#define kNotidenyupdate_Gubun            @"gubun"
#define NOTI_UPDATE_ITEM_ATTREBUTE [[NSArray alloc] initWithObjects:kNotidenyupdateRoot_Item, kNotidenyupdate_Item, kNotidenyupdate_Gubun, nil]

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.activityView = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
    activityView.center = self.view.center;
    //[activityView setBackgroundColor:[UIColor whiteColor]];
    [activityView setColor:UIColorFromRGB(0XE20167)];
    
    [self.view addSubview:activityView];

//    어플 실행시에만 updateDeviceUser 호출하도록수정 2014.10.06오창욱
//    NSString *loginedID = [[NSUserDefaults standardUserDefaults] stringForKey:@"LoginID"];
//    [self updateDeviceUser:loginedID];
    
    CGRect tableViewFrame = CGRectMake(self.view.frame.origin.x, self.view.frame.origin.y, self.view.frame.size.width, self.view.frame.size.height);
    
    
    self.tableView = [[UITableView alloc] initWithFrame:tableViewFrame style:UITableViewStyleGrouped];
    self.tableView.autoresizingMask = (UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight);
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleSingleLine;
    
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    
    self.tableView.backgroundColor = UIColorFromRGB(0XE5E5E5);
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.tableView.separatorColor =  UIColorFromRGB(0XE5E5E5);
    
    //[self.contentView addSubview:self.tableView];

    
    UILabel *titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 320, 30)];
    titleLabel.textColor = [UIColor whiteColor];
    titleLabel.text = @"            알림 설정";
    //titleLabel.textAlignment = NSTextAlignmentCenter;
    [self.navigationItem setTitleView:titleLabel];
    
    [[UINavigationBar appearance] setBackgroundImage:[UIImage imageNamed:@"topbg.png"] forBarMetrics:UIBarMetricsDefault];
    
    UIImage *backImage = [UIImage imageNamed:@"btn_back.png"];
    UIButton *backButton = [UIButton buttonWithType:UIButtonTypeCustom];
    backButton.frame = CGRectMake(-1000, 0, backImage.size.width, backImage.size.height);
    
    [backButton setImage:backImage forState:UIControlStateNormal];
    UIBarButtonItem *backBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:backButton];
    
    [backButton addTarget:self action:@selector(pushBackButton:)    forControlEvents:UIControlEventTouchUpInside];

    self.navigationItem.hidesBackButton = YES;
    self.navigationItem.leftBarButtonItem = backBarButtonItem;
    
    self.tableView.backgroundColor = UIColorFromRGB(0XE5E5E5);
    self.tableView.backgroundView = nil;
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    
}

-(void) pushBackButton:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    
    NSHTTPCookie *cookie;
    NSArray* arrCookies = [[NSHTTPCookieStorage sharedHTTPCookieStorage] cookies];
    BOOL checkLogin = NO;
    
    //Login 쿠키값을 체크한다.
    for (cookie in arrCookies)
    {
        if ([[cookie name] isEqualToString:@"loginToken"]) {
            checkLogin = YES;
        }
    }
    
    if (checkLogin)
    {
        isLoginInfo = YES;
    }
    else
    {
        isLoginInfo = NO;
    }
    
    self.activityView.hidden = NO;
	[activityView startAnimating];
    
    [self notiContentView];
    
    self.activityView.hidden = YES;
	[activityView stopAnimating];

    [self.tableView reloadData];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSNumber*)updateDeviceUser:(NSString*)userID
{
    NSString *deviceToken = [[NSUserDefaults standardUserDefaults] objectForKey:@"deviceToken"];
    
    NSString *strURL = [NSString stringWithFormat:@"%@?act=updateDeviceUser&phonetype=0&token=%@&userid=%@&version=%@", LibURL, deviceToken, userID ,[[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"]];
    
    XmlParser *xmlParser = [[XmlParser alloc] initParser:[NSURL URLWithString:strURL] andWithItems:NOTI_DEVICEUSER_ITEM_ATTREBUTE andWithParserType:(ParserType *)Parser1];

    if (xmlParser.parsedItems && [xmlParser.parsedItems count] > 0)
    {
        NSString *gubun = [[xmlParser.parsedItems objectAtIndex:0] objectForKey:kNotiDeviceUSER_Gubun];
        
//        NSLog(@"updateDeviceUser_Gubun : %@", gubun);
        
        if ([gubun isEqualToString:@"COMPLETE"]) {
            return [[NSNumber alloc] initWithBool:YES];
        }
        
    }

    return [[NSNumber alloc] initWithBool:NO];
}

- (NSMutableArray *)notiSetList
{
    NSString *deviceToken = [[NSUserDefaults standardUserDefaults] objectForKey:@"deviceToken"];
    NSString *strURL = [NSString stringWithFormat:@"%@?act=getDenyList&token=%@", LibURL, deviceToken];
    
    XmlParser *xmlParser = [[XmlParser alloc] initParser:[NSURL URLWithString:strURL] andWithItems:NOTISET_DETAIL_ATTREBUTE andWithParserType:(ParserType *)Parser3];
    
    return xmlParser.parsedItems;
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    switch (section)
    {
        case 0:
            return 2;
            break;
            
        case 1:
            return 3;
            break;
    }
    
    return 0;
}

-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    UIImage *image = nil;

    if (indexPath.section == 0)
    {
        if (indexPath.row == 1)
        {
            image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"mybox_bottom" ofType:@"png"]];
        }
        else if (indexPath.row == 4)
        {
            image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"mybox_bottom" ofType:@"png"]];
        }
        else
        {
            image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"mybox_top" ofType:@"png"]];
        }
    }
    else if (indexPath.section == 1)
    {
        
        if (indexPath.row == 0)
        {
            image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"mybox_top" ofType:@"png"]];
        }
        else if (indexPath.row == 2)
        {
            image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"mybox_bottom" ofType:@"png"]];
        }
        else
        {
            image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"mybox_top" ofType:@"png"]];
        }
    }
    
    UIImageView *imgView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 320, 47)];
    [imgView setImage:image];
    cell.backgroundView = imgView;
}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    if(section == 0)
        return 40.0;
    else
        return 40.0;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(indexPath.section == 0)
    {
        switch (indexPath.row)
        {
            case 0:
                return 0;
                break;
            default:
                return 50;
                break;
        }
    }else
    {
        return 50;
    }
}

-(UIView *)tableView:(UITableView *)tView viewForHeaderInSection:(NSInteger)section
{
    UILabel * lblTitle1 = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 320, 40)];
    
    [lblTitle1 setFont:[UIFont boldSystemFontOfSize:15]];
    lblTitle1.text = @"   알림 설정 항목";
    lblTitle1.backgroundColor = UIColorFromRGB(0XE5E5E5);
    lblTitle1.textAlignment = NSTextAlignmentLeft;
    lblTitle1.textColor = FONT_COLOR_BLACK;
    
    UILabel * lblTitle2 = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 320, 40)];
    [lblTitle2 setFont:[UIFont boldSystemFontOfSize:15]];
    lblTitle2.text = @"   알림 설정 옵션";
    lblTitle2.backgroundColor = UIColorFromRGB(0XE5E5E5);
    lblTitle2.textAlignment = NSTextAlignmentLeft;
    lblTitle2.textColor = FONT_COLOR_BLACK;
    
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 40)];
    
    switch (section)
    {
        case 0 :
            [view addSubview:lblTitle1];
            break;
        case 1 :
            [view addSubview:lblTitle2];
            break;
    }
    
    return view;
}

-(CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 0;
}

-(UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    if(section == 0)
    {
        UILabel * lblTitle1 = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 320, 60)];
        [lblTitle1 setFont:[UIFont  boldSystemFontOfSize:12.0]];
        lblTitle1.text = @"   생일/결혼 기념일 알림 설정 시 축하 쿠폰 등의 고객 혜택을\n   받으실 수 있습니다.";
        lblTitle1.numberOfLines = 3;
        lblTitle1.backgroundColor = UIColorFromRGB(0XE5E5E5);
        lblTitle1.textAlignment = NSTextAlignmentLeft;
        lblTitle1.textColor = FONT_COLOR_BLACK;
        
        UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 110)];
        
        //[view addSubview:lblTitle1];
        
        return view;
    }
    
    return nil;
}

- (UITableViewCell *)tableView:(UITableView *)table cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [table dequeueReusableCellWithIdentifier:@"NotiSetCell"];
    
    cell.backgroundColor = UIColorFromRGB(0XE5E5E5);
    
    if (cell == nil)
    {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:@"NotiSoundSetCell"];
    }
    
    if (indexPath.section == 0)
    {
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        /*
        if (indexPath.row == 1)
        {
            cell.textLabel.text = @" 아싸 특가 알림";
            cell.textLabel.font = [UIFont systemFontOfSize:15.0];
            
            UISwitch *notiSwitch = [[UISwitch alloc] initWithFrame:CGRectMake(0.0, 0.0, 51.0, 31.0)];
            notiSwitch.tintColor = UIColorFromRGB(0XE5E5E5);
            notiSwitch.onTintColor = UIColorFromRGB(0xE10064);
            notiSwitch.layer.cornerRadius = 16.0;
            notiSwitch.tag = 5; //eid
            
            NSString *notiValue = @"0";
            @try {
                NSMutableDictionary *items = [[NSMutableDictionary alloc] initWithDictionary:[self.arrNotiItems objectAtIndex:4]];
                NSLog(@"cshbi= %@", [kNotisetElements objectForKey:@"deny"]);
                notiValue = [items objectForKey:[[kNotisetElements objectForKey:@"deny"] objectAtIndex:2]];
            }
            @catch (NSException *exception) {
                NSLog(@"err");
            }
            
            if ([notiValue isEqualToString:@"1"]) {
                [notiSwitch setOn:NO animated:NO];
            }else
            {
                [notiSwitch setOn:YES animated:NO];
            }
            [notiSwitch addTarget:self action:@selector(switchAction:) forControlEvents:UIControlEventTouchUpInside];
            cell.accessoryView = notiSwitch;
            [cell setHidden:NO];
             
        } else if (indexPath.row == 2)
        */
        if (indexPath.row ==1)
        {
            cell.textLabel.text = @" PUSH 알림 받기";
            cell.textLabel.font = [UIFont systemFontOfSize:15.0];
            
            UISwitch *notiSwitch = [[UISwitch alloc] initWithFrame:CGRectMake(0.0, 0.0, 51.0, 31.0)];
            notiSwitch.tintColor = UIColorFromRGB(0XE5E5E5);
            notiSwitch.onTintColor = UIColorFromRGB(0xE10064);
            notiSwitch.layer.cornerRadius = 16.0;
            notiSwitch.tag = 3; //eid
            
            NSString *notiValue = @"0";
            @try {
                NSMutableDictionary *items = [[NSMutableDictionary alloc] initWithDictionary:[self.arrNotiItems objectAtIndex:0]];
                notiValue = [items objectForKey:[[kNotisetElements objectForKey:@"deny"] objectAtIndex:2]];
            }
            @catch (NSException *exception) {
            }
            
            if ([notiValue isEqualToString:@"1"]) {
                [notiSwitch setOn:NO animated:NO];
            }else
            {
                [notiSwitch setOn:YES animated:NO];
            }
            [notiSwitch addTarget:self action:@selector(switchAction:) forControlEvents:UIControlEventTouchUpInside];
            cell.accessoryView = notiSwitch;
        }
        /*else if (indexPath.row == 3)
        {
            cell.textLabel.text = @" 생일 알림";
            cell.textLabel.font = [UIFont systemFontOfSize:15.0];
            cell.detailTextLabel.font = [UIFont systemFontOfSize:12.0];
            cell.detailTextLabel.textColor = FONT_COLOR_PINK;
            
            //스위치
            UISwitch *notiSwitch = [[UISwitch alloc] initWithFrame:CGRectMake(0.0, 0.0, 51.0, 31.0)];
            notiSwitch.tintColor = UIColorFromRGB(0XE5E5E5);
            notiSwitch.onTintColor = UIColorFromRGB(0xE10064);
            notiSwitch.layer.cornerRadius = 16.0;
            notiSwitch.tag = 1; //eid
            NSString *notiValue = @"0";
            
            @try {
                NSMutableDictionary *items = [[NSMutableDictionary alloc] initWithDictionary:[self.arrNotiItems objectAtIndex:0]];
                notiValue = [items objectForKey:[[kNotisetElements objectForKey:@"deny"] objectAtIndex:2]];
            }
            @catch (NSException *exception) {
            }
            
            //최초 인증 했음. (on/off 맘대로~)
            NSString *firstAuthID = [[NSUserDefaults standardUserDefaults] objectForKey:@"FirstAuthID"];
            if (firstAuthID)
            {
                NSString *loginedID = nil;
                //로그인 되있음.
                if (isLoginInfo)
                {
                    loginedID = [[NSUserDefaults standardUserDefaults] objectForKey:@"LoginID"];
                    
                    //로그인 된 아이디값 == 최초 인증 ID값
                    if ([firstAuthID isEqualToString:loginedID])
                    {
                        if (self.arrNotiSpecialdayItems && [self.arrNotiSpecialdayItems count]> 0)
                        {
                            NSMutableDictionary *items1 = [[NSMutableDictionary alloc] initWithDictionary:[self.arrNotiSpecialdayItems objectAtIndex:0]];
                            
                            NSString *notiBath = [items1 objectForKey:[[kNotisetElements objectForKey:@"specialday"] objectAtIndex:0]];
                            NSString *notiSolar = [items1 objectForKey:[[kNotisetElements objectForKey:@"specialday"] objectAtIndex:2]];
                            
                            NSString *notiSolarGb = [notiSolar isEqualToString:@"sun" ] ? @"양" : @"음";
                            
                            if ([notiBath length] <= 0 || [notiSolar length] <= 0)
                            {
                                cell.detailTextLabel.text = @"생일정보가 없습니다.";
                            } else
                            {
                                cell.detailTextLabel.text = [NSString stringWithFormat:@" %@ (%@)",[notiBath stringByReplacingOccurrencesOfString:@"-" withString:@"."], notiSolarGb];
                            }
                        }
                    }
                    //로그인 된 아이디값 != 최초 인증 ID값
                    else
                    {
                        //다른 아이디입니다.
                        //생일 설정 이용하시겠습니까?
                        MSG_DELEGATE_BTN2(@"다른 아이디로 로그인하셨습니다.", @"취소", @"확인", kNotiBathMsgGubun, self);
                        
                    }
                }
                //로그인이 안되있으면
                else
                {
                    //최초인증은 되있지만, 로그인이 안되있을 경우.
                    cell.detailTextLabel.text = @" 설정됨";
                }
                
                //그런데 notiValue == 1이면 미설정
                if ([notiValue isEqualToString:@"1"])
                {
                    [notiSwitch setOn:NO animated:NO];
                    cell.detailTextLabel.text = @" 미설정";
                }else
                {
                    [notiSwitch setOn:YES animated:NO];
                }
            }
            //최초 인증 안했음.
            else
            {
                //최초 인증안했으면 Data가 YES여도 미설정.. (어플 지우고 새로 받았을경우 DB값이랑 안맞을 수도 있다.)
                [notiSwitch setOn:NO animated:NO];
                cell.detailTextLabel.text = @" 미설정";
            }
            
            
            [notiSwitch addTarget:self action:@selector(switchAction:) forControlEvents:UIControlEventTouchUpInside];
            cell.accessoryView = notiSwitch;
        }
        else if (indexPath.row == 4)
        {
            cell.textLabel.text = @" 결혼기념일 알림";
            cell.textLabel.font = [UIFont systemFontOfSize:15.0];
            cell.detailTextLabel.font = [UIFont systemFontOfSize:12.0];
            cell.detailTextLabel.textColor = FONT_COLOR_PINK;
            
            //스위치
            UISwitch *notiSwitch = [[UISwitch alloc] initWithFrame:CGRectMake(0.0, 0.0, 51.0, 31.0)];
            notiSwitch.tintColor = UIColorFromRGB(0XE5E5E5);
            notiSwitch.onTintColor = UIColorFromRGB(0xE10064);
            notiSwitch.layer.cornerRadius = 16.0;
            notiSwitch.tag = 2; //eid
            
            NSString *notiValue = @"0";
            @try {
                NSMutableDictionary *items = [[NSMutableDictionary alloc] initWithDictionary:[self.arrNotiItems objectAtIndex:1]];
                notiValue = [items objectForKey:[[kNotisetElements objectForKey:@"deny"] objectAtIndex:2]];
            }
            @catch (NSException *exception) {
            }
            
            //최초 인증 했음. (on/off 맘대로~)
            NSString *firstAuthID = [[NSUserDefaults standardUserDefaults] objectForKey:@"FirstAuthID"];
            if (firstAuthID)
            {
                NSString *loginedID = nil;
                //로그인 되있음.
                if (isLoginInfo)
                {
                    loginedID = [[NSUserDefaults standardUserDefaults] objectForKey:@"LoginID"];
                    
                    //로그인 된 아이디값 == 최초 인증 ID값
                    if ([firstAuthID isEqualToString:loginedID])
                    {
                        if (self.arrNotiSpecialdayItems && [self.arrNotiSpecialdayItems count]> 0)
                        {
                            NSMutableDictionary *items1 = [[NSMutableDictionary alloc] initWithDictionary:[self.arrNotiSpecialdayItems objectAtIndex:0]];
                            
                            NSString *notiWeddingDate = [items1 objectForKey:[[kNotisetElements objectForKey:@"specialday"] objectAtIndex:1]];
                            
                            if ([notiWeddingDate length] <= 0) {
                                cell.detailTextLabel.text = @" 결혼 기념일 정보가 없습니다.";
                            }
                            else
                            {
                                cell.detailTextLabel.text = [notiWeddingDate stringByReplacingOccurrencesOfString:@"-" withString:@"."];
                            }
                        }
                    }
                }
                //로그인이 안되있으면
                else
                {
                    //최초인증은 되있지만, 로그인이 안되있을 경우.
                    cell.detailTextLabel.text = @" 설정됨";
                }
                
                //그런데 notiValue == 1이면 미설정
                if ([notiValue isEqualToString:@"1"])
                {
                    [notiSwitch setOn:NO animated:NO];
                    cell.detailTextLabel.text = @" 미설정";
                }else
                {
                    [notiSwitch setOn:YES animated:NO];

                }
            }
            //최초 인증 안했음.
            else
            {
                //최초 인증 Flag(로그인 한 아이디)값이 비어있는 경우
                // 1.인증받았다가 어플을 삭제했다. (그래서 NSUserDefaults값이 사라졌지만 DB에는 YES로 되어있따)
                // 2. 정말 최초 인증을 안했다.
                
                //최초 인증안했으면 Data가 YES여도 미설정.. (어플 지우고 새로 받았을경우 DB값이랑 안맞을 수도 있다.)
                [notiSwitch setOn:NO animated:NO];
                cell.detailTextLabel.text = @" 미설정";
            }
            
            [notiSwitch addTarget:self action:@selector(switchAction:) forControlEvents:UIControlEventTouchUpInside];
            
            cell.accessoryView = notiSwitch;
        }
         */
    }
    //2번째 indexPath
    else
    {
        if (indexPath.row == 0 || indexPath.row == 1)
        {
            NSString *notiStartDate = @"00";
            NSString *notiEndDate = @"00";
            
            @try {
                NSMutableDictionary *items = [[NSMutableDictionary alloc] initWithDictionary:[self.arrNotiDenytimeItems objectAtIndex:0]];
                notiStartDate = [items objectForKey:[[kNotisetElements objectForKey:@"denytime"] objectAtIndex:0]];
                notiEndDate = [items objectForKey:[[kNotisetElements objectForKey:@"denytime"] objectAtIndex:1]];
            }
            @catch (NSException *exception) {
            }
            
            BOOL isSetting = NO;
            
            if(notiStartDate == nil || notiEndDate == nil){
                isSetting = NO;
            }else if ([notiStartDate isEqualToString:@"00"] || [notiEndDate isEqualToString:@"00"]){
                isSetting = NO;
            }else{
                isSetting = YES;
            }
            
            if (indexPath.row == 0)
            {
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                cell.textLabel.text = @" 알림 금지 시간대 설정";
                cell.textLabel.font = [UIFont systemFontOfSize:15.0];
                
                UISwitch *notiSwitch = [[UISwitch alloc] initWithFrame:CGRectMake(0.0, 0.0, 51.0, 31.0)];
                notiSwitch.tintColor = UIColorFromRGB(0XE5E5E5);
                notiSwitch.onTintColor = UIColorFromRGB(0xE10064);
                notiSwitch.layer.cornerRadius = 16.0;
                notiSwitch.tag = 6;
                
                if (isSetting)
                {
                    [notiSwitch setOn:YES animated:NO];
                }else
                {
                    [notiSwitch setOn:NO animated:NO];
                }
                [notiSwitch addTarget:self action:@selector(switchAction:) forControlEvents:UIControlEventTouchUpInside];
                cell.accessoryView = notiSwitch;
            }
            else
            {
                if (isSetting)
                {
                    cell.selectionStyle = UITableViewCellSelectionStyleGray;
                    NSDictionary* dicTime = [NSDictionary dictionaryWithObjectsAndKeys:
                                             @"am 1:00", @"01",
                                             @"am 2:00", @"02",
                                             @"am 3:00", @"03",
                                             @"am 4:00", @"04",
                                             @"am 5:00", @"05",
                                             @"am 6:00", @"06",
                                             @"am 7:00", @"07",
                                             @"am 8:00", @"08",
                                             @"am 9:00", @"09",
                                             @"am 10:00", @"10",
                                             @"am 11:00", @"11",
                                             @"am 12:00", @"12",
                                             
                                             @"pm 1:00", @"13",
                                             @"pm 2:00", @"14",
                                             @"pm 3:00", @"15",
                                             @"pm 4:00", @"16",
                                             @"pm 5:00", @"17",
                                             @"pm 6:00", @"18",
                                             @"pm 7:00", @"19",
                                             @"pm 8:00", @"20",
                                             @"pm 9:00", @"21",
                                             @"pm 10:00",@"22",
                                             @"pm 11:00",@"23",
                                             @"pm 12:00",@"24",
                                             nil];
                    NSString *strStartAMPM = [dicTime objectForKey:notiStartDate];
                    NSString *strEndAMPM = [dicTime objectForKey:notiEndDate];;
                    
                    cell.textLabel.text = [NSString stringWithFormat:@" %@ ~ %@",strStartAMPM, strEndAMPM];
                    cell.detailTextLabel.text = @" 재설정";
                    
                    UIImage* image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"arrow_black_right" ofType:@"png"]];
                    UIImageView* imgView = [[UIImageView alloc] initWithImage:image];
                    cell.accessoryView = imgView;
                }
                else
                {
                    cell.selectionStyle = UITableViewCellSelectionStyleNone;
                    cell.textLabel.text = @" 미설정";
                }
                cell.textLabel.font = [UIFont systemFontOfSize:15.0];
                cell.textLabel.textColor = FONT_COLOR_PINK;
            }
        }
        else if (indexPath.row == 2)
        {
            cell.textLabel.text = @" 알림 소리/진동 설정";
            cell.textLabel.font = [UIFont systemFontOfSize:15.0];
            
            int nNotiSound = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"notiSound"];
            
            //설정값이랑 같으면 표시
            //디폴트는 무음.
            //어플 실행 중일때 알림소리 .  @"무음", @"진동", @"알림소리"
            if (nNotiSound == 0)
            {
                cell.detailTextLabel.text = @" 무음";
            }else if (nNotiSound == 1)
            {
                cell.detailTextLabel.text = @" 진동";
            }else if (nNotiSound == 2)
            {
                cell.detailTextLabel.text = @" 알림소리";
            }
            
            cell.detailTextLabel.font = [UIFont systemFontOfSize:12.0];
            cell.detailTextLabel.textColor = FONT_COLOR_PINK;
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            //액세서리 뷰 이미지 바꾸기~
            UIImage* image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"arrow_black_right" ofType:@"png"]];
            UIImageView* imgView = [[UIImageView alloc] initWithImage:image];
            cell.accessoryView = imgView;
        }
    }
    
    cell.textLabel.backgroundColor = [UIColor clearColor];
    cell.detailTextLabel.backgroundColor = [UIColor clearColor];
    
    return cell;
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == 0)
    {
        //최초 인증 Flag null 처리한다,
        [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"FirstAuthID"];
        
        self.activityView.hidden = NO;
        [activityView startAnimating];
        
        [self notiSetUpdate:@"1" andWithValue:@"1"];
        [self notiSetUpdate:@"2" andWithValue:@"1"];
        
        self.activityView.hidden = YES;
        [activityView stopAnimating];
    }
    else
    {
        //로그인은 이미 되있음.
        NSString *loginedID = nil;
        //로그인 되있음.
        if (isLoginInfo)
        {
            loginedID = [[NSUserDefaults standardUserDefaults] objectForKey:@"LoginID"];
        }
        
        self.activityView.hidden = NO;
        [activityView startAnimating];

//    어플 실행시에만 updateDeviceUser 호출하도록수정 2014.10.06오창욱
//        [self updateDeviceUser:loginedID];
        [self notiSetUpdate:@"1" andWithValue:@"1"];
        [self notiSetUpdate:@"2" andWithValue:@"0"];
        
        self.activityView.hidden = YES;
        [activityView stopAnimating];
        
        //3. 최초 인증 Flag값에 로그인 된 새로운 아이디값으로 바꿔준다.
        [[NSUserDefaults standardUserDefaults] setValue:loginedID forKey:@"FirstAuthID"];
    }
    
    [self notiContentView];
}

- (void)switchAction:(id)sender
{
    if (!self.activityView.isAnimating)
    {
        UISwitch *sw = sender;
        
        //YES : 0
        //NO : 1
        //0 : 이벤트 허용 ,1 : 이벤트 거부
        if (sw.tag == 6)
        {
            if (sw.isOn)
            {
                //금지시간 사용함.
                //금지시간 설정화면에서 시간 설정한뒤 [저장]버튼 클릭하면 업데이트
                
                
                
                //금지 시간대 설정 뷰 이동
//                NSLog(@"금지 시간대 설정뷰 이동");
                [self performSegueWithIdentifier: @"TimeSetting" sender: self];
            }
            else
            {
                //금지시간 사용안함.
                self.activityView.hidden = NO;
                [activityView startAnimating];
                
                [self notiSetTimeUpdate:@"00" andWithTime:@"00"];
                
                self.activityView.hidden = YES;
                [activityView stopAnimating];
            }
        }
        else
        {
            NSString* eid = [NSString stringWithFormat:@"%ld", (long)sw.tag];
//            NSLog(@"cshbi = %d", sw.isOn);
            NSString* value = sw.isOn ? @"0" : @"1";
            
            BOOL isUpdateDeny = NO;
            
            if (sw.tag == 1 || sw.tag == 2)
            {
                NSString *firstAuthID = [[NSUserDefaults standardUserDefaults] objectForKey:@"FirstAuthID"];
                
                if (firstAuthID)
                {
                    //최초 인증 했음. (on/off 맘대로~)
                    isUpdateDeny = YES;
                }
                else
                {
                    //로그인 했고 on이면
                    if (isLoginInfo  && [value isEqualToString:@"0"])
                    {
                        //로그인은 이미 되있음.
                        NSString *loginedID = nil;
                        //로그인 되있음.
                        //if (isLoginInfo) {
                        loginedID = [[NSUserDefaults standardUserDefaults] objectForKey:@"LoginID"];
                        //}
                        
                        //최조 인증
                        self.activityView.hidden = NO;
                        [activityView startAnimating];
  
//    어플 실행시에만 updateDeviceUser 호출하도록수정 2014.10.06오창욱
//                        [self updateDeviceUser:loginedID];
                        
                        self.activityView.hidden = YES;
                        [activityView stopAnimating];
                        
                        isUpdateDeny = YES;

                        [[NSUserDefaults standardUserDefaults] setValue:loginedID forKey:@"FirstAuthID"];
                    }
                    //로그인 안했거나 , off일경우
                    else
                    {
                        isUpdateDeny = YES;
                    }
                }
            }
            else
            {
                isUpdateDeny = YES;
            }
            
            if (isUpdateDeny)
            {
                self.activityView.hidden = NO;
                [activityView startAnimating];
                
                [self notiSetUpdate:eid andWithValue:value];
                
                self.activityView.hidden = YES;
                [activityView stopAnimating];
            }
        }
        
        [self notiContentView];
    }
}

- (void)notiContentView
{
    self.arrNotilist = [self notiSetList];
    
    if (self.arrNotilist && [self.arrNotilist count] > 0)
    {
        self.arrNotiItems =  [self.arrNotilist objectAtIndex:0];
        self.arrNotiSpecialdayItems = [self.arrNotilist objectAtIndex:1];
        self.arrNotiDenytimeItems = [self.arrNotilist objectAtIndex:2];
    }

    [self.tableView reloadData];
}

- (NSNumber*)notiSetTimeUpdate:(NSString*)starthh andWithTime:(NSString*)endhh
{
    //NSString *strURL = [NSString stringWithFormat:@"%@?act=getDenyList", kLibURL];
    
    NSString *deviceToken = [[NSUserDefaults standardUserDefaults] objectForKey:@"deviceToken"];
    
    NSString *strURL = [NSString stringWithFormat:@"%@?act=updateDeny&type=denytime&starthh=%@&endhh=%@&token=%@", LibURL, starthh, endhh, deviceToken];
    
    XmlParser *xmlParser = [[XmlParser alloc] initParser:[NSURL URLWithString:strURL] andWithItems:NOTI_UPDATE_ITEM_ATTREBUTE andWithParserType:(ParserType *)Parser1];
    
    if (xmlParser.parsedItems && [xmlParser.parsedItems count] > 0)
    {
        NSString *gubun = [[xmlParser.parsedItems objectAtIndex:0] objectForKey:kNotidenyupdate_Gubun];

        if ([gubun isEqualToString:@"COMPLETE"]) {
//            NSLog(@"notiSetUpdate_Gubun : %@", gubun);
            return [[NSNumber alloc] initWithBool:YES];
        }
    }

    return [[NSNumber alloc] initWithBool:NO];
}

- (NSNumber*)notiSetUpdate:(NSString*)eid andWithValue:(NSString*)deny
{
    NSString *deviceToken = [[NSUserDefaults standardUserDefaults] objectForKey:@"deviceToken"];
    NSString *strURL = [NSString stringWithFormat:@"%@?act=updateDeny&eid=%@&deny=%@&type=alarm&token=%@", LibURL, eid, deny, deviceToken];
    
    XmlParser *xmlParser = [[XmlParser alloc] initParser:[NSURL URLWithString:strURL] andWithItems:NOTI_UPDATE_ITEM_ATTREBUTE andWithParserType:(ParserType *)Parser1];

    if (xmlParser.parsedItems && [xmlParser.parsedItems count] > 0)
    {
        NSString *gubun = [[xmlParser.parsedItems objectAtIndex:0] objectForKey:kNotidenyupdate_Gubun];
    
        if ([gubun isEqualToString:@"COMPLETE"])
        {
//            NSLog(@"notiSetUpdate_Gubun : %@", gubun);
            return [[NSNumber alloc] initWithBool:YES];
        }
    }
    
    return [[NSNumber alloc] initWithBool:NO];
}

-(void)tableView:(UITableView *)tView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0) {
        
    }
    else if (indexPath.section == 1) {
        if (indexPath.row == 1) {
            //금지시간 사용함.
            //금지시간 설정화면에서 시간 설정한뒤 [저장]버튼 클릭하면 업데이트
            
            UITableViewCell * cell = [tView cellForRowAtIndexPath:indexPath];
            
            NSString *strCell = cell.textLabel.text;
            
            if (![strCell isEqualToString:@" 미설정"]) {
                [self performSegueWithIdentifier: @"TimeSetting" sender: self];
            }
        }
        else if (indexPath.row == 2) {
            [self performSegueWithIdentifier: @"SoundSetting" sender: self];
        }
        
    }
    
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([segue.identifier isEqualToString:@"TimeSetting"]) {
        NSMutableDictionary *items = [[NSMutableDictionary alloc] initWithDictionary:[self.arrNotiDenytimeItems objectAtIndex:0]];
        
        NSString *notiStartDate = [items objectForKey:[[kNotisetElements objectForKey:@"denytime"] objectAtIndex:0]];
        NSString *notiEndDate = [items objectForKey:[[kNotisetElements objectForKey:@"denytime"] objectAtIndex:1]];
        
        TimeSettingTableTableViewController *destViewController = segue.destinationViewController;
        destViewController.strStartDate = notiStartDate;
        destViewController.strEndDate = notiEndDate;
    }
}
@end
