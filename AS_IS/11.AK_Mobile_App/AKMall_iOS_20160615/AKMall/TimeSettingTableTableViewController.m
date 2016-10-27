//
//  TimeSettingTableTableViewController.m
//  AKMall
//
//  Created by 한병일 on 2014. 6. 22..
//  Copyright (c) 2014년 한병일. All rights reserved.
//

#import "TimeSettingTableTableViewController.h"

@interface TimeSettingTableTableViewController ()

@end

@implementation TimeSettingTableTableViewController
@synthesize strEndDate;
@synthesize strStartDate;
@synthesize notiSwitch;

#define FONT_COLOR_PINK     [UIColor colorWithRed:0.824 green:0.000 blue:0.412 alpha:1.000]
#define FONT_COLOR_BLACK    [UIColor colorWithWhite:0.200 alpha:1.000]

#define kCellTitle_start @"시작시간"
#define kCellTitle_end @"종료시간"
#define kStartMsg @"미설정           "
#define kEndMsg @"미설정           "

#define CELL_MSG [[NSArray alloc] initWithObjects:kCellTitle_start, kCellTitle_end, nil]
#define CELL_DETAIL_MSG [[NSArray alloc] initWithObjects:kStartMsg, kEndMsg, nil]

#define kAM @"am"
#define kPM @"pm"

#define MSG_NORMAL(TITLE, FRIST_BUTTON_TITLE) [[[UIAlertView alloc] initWithTitle:TITLE message:nil delegate:nil cancelButtonTitle:FRIST_BUTTON_TITLE otherButtonTitles:nil, nil] show]

#define kNotidenyupdateRoot_Item         @"root"
#define kNotidenyupdate_Item             @"denyupdate"
#define kNotidenyupdate_Gubun            @"gubun"

#define NOTI_UPDATE_ITEM_ATTREBUTE [[NSArray alloc] initWithObjects:kNotidenyupdateRoot_Item, kNotidenyupdate_Item, kNotidenyupdate_Gubun, nil]

#define ALIM_TITLE_MSG @"설정을 저장하시겠습니까?"
#define MSG_DELEGATE_BTN2(TITLE, CANCEL_BUTTON_TITLE, OK_BUTTON_TITLE, MSG, DELEGATE) [[[UIAlertView alloc] initWithTitle:TITLE message:MSG delegate:DELEGATE cancelButtonTitle:CANCEL_BUTTON_TITLE otherButtonTitles:OK_BUTTON_TITLE, nil] show]
#define TIME_SHEET_ARR @[@"am 1:00",@"am 2:00",@"am 3:00",@"am 4:00",@"am 5:00",@"am 6:00",@"am 7:00",@"am 8:00",@"am 9:00",@"am 10:00",@"am 11:00",@"am 12:00",@"pm 1:00",@"pm 2:00",@"pm 3:00",@"pm 4:00",@"pm 5:00",@"pm 6:00",@"pm 7:00",@"pm 8:00",@"pm 9:00",@"pm 10:00",@"pm 11:00",@"pm 12:00"]

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
    
    CGRect tableViewFrame = CGRectMake(self.view.frame.origin.x, self.view.frame.origin.y, self.view.frame.size.width, self.view.frame.size.height);;
    self.tableView = [[UITableView alloc] initWithFrame:tableViewFrame style:UITableViewStyleGrouped];
    self.tableView.autoresizingMask = (UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight);
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleSingleLine;
    
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    
    self.tableView.backgroundColor = UIColorFromRGB(0XE5E5E5);
    
    self.tableView.scrollEnabled = NO;
    
    UILabel *titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 320, 30)];
    titleLabel.textColor = [UIColor whiteColor];
    titleLabel.text = @"     알림 금지 시간대 설정";
    //titleLabel.textAlignment = NSTextAlignmentCenter;
    [self.navigationItem setTitleView:titleLabel];
    
    [[UINavigationBar appearance] setBackgroundImage:[UIImage imageNamed:@"topbg.png"] forBarMetrics:UIBarMetricsDefault];
    
    UIImage *backImage = [UIImage imageNamed:@"btn_back.png"];
    UIButton *backButton = [UIButton buttonWithType:UIButtonTypeCustom];
    backButton.frame = CGRectMake(0, 0, backImage.size.width, backImage.size.height);
    
    [backButton setImage:backImage forState:UIControlStateNormal];
    UIBarButtonItem *backBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:backButton];
    [backButton addTarget:self action:@selector(pushBackButton:)    forControlEvents:UIControlEventTouchUpInside];
    self.navigationItem.hidesBackButton = YES;
    self.navigationItem.leftBarButtonItem = backBarButtonItem;
    
    UIBarButtonItem *anotherButton = [[UIBarButtonItem alloc] initWithTitle:@"저장" style:UIBarButtonItemStyleDone target:self action:@selector(doSave:)];
    anotherButton.tintColor = [UIColor whiteColor];
    self.navigationItem.rightBarButtonItem = anotherButton;
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [self.tableView reloadData];
}

-(void) pushBackButton:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 2;
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    UIImage *image = nil;
    
    if (indexPath.row == 0)
    {
        image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"my_box_top" ofType:@"png"]];
    }
    else if (indexPath.row == 1)
    {
        image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"my_box_bottom" ofType:@"png"]];
    }
    
    UIImageView *imgView = [[UIImageView alloc] initWithImage:image];
    
    cell.backgroundView = imgView;
    
    cell.detailTextLabel.font = [UIFont systemFontOfSize:12.0];
}

- (UITableViewCell *)tableView:(UITableView *)table cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [table dequeueReusableCellWithIdentifier:@"NotiSetCell"];
    
    if (cell == nil)
    {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:@"NotiSetCell"];
    }
    
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
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    cell.textLabel.text = [CELL_MSG objectAtIndex:indexPath.row];
    cell.textLabel.backgroundColor = [UIColor clearColor];
    
    cell.detailTextLabel.text = [CELL_DETAIL_MSG objectAtIndex:indexPath.row];
    
    BOOL isSetting = NO;
    
    
    if(self.strStartDate == nil || self.strEndDate == nil){
        isSetting = NO;
    }else if ([self.strStartDate isEqualToString:@"00"] || [self.strEndDate isEqualToString:@"00"]){
        isSetting = NO;
    }else{
        isSetting = YES;
    }
    
    
    if (indexPath.row == 0)
    {
        cell.detailTextLabel.text = isSetting ? [dicTime objectForKey:self.strStartDate] : [CELL_DETAIL_MSG objectAtIndex:indexPath.row];
    }
    else if (indexPath.row == 1)
    {
        cell.detailTextLabel.text = isSetting ? [dicTime objectForKey:self.strEndDate] : [CELL_DETAIL_MSG objectAtIndex:indexPath.row];
    }
    
    cell.detailTextLabel.font = [UIFont systemFontOfSize:12.0];
    cell.detailTextLabel.textColor = FONT_COLOR_PINK;
    cell.detailTextLabel.backgroundColor = [UIColor clearColor];
    cell.textLabel.textColor = FONT_COLOR_BLACK;
    
    // Size doesn't matter
    self.notiSwitch = [UIButton buttonWithType:UIButtonTypeCustom];
    self.notiSwitch.frame = CGRectMake(0, 0, 32, 32);
    self.notiSwitch.tag = indexPath.row;
    
    if (![cell.detailTextLabel.text isEqualToString:[CELL_DETAIL_MSG objectAtIndex:indexPath.row]])
    {
        //설정
        [self.notiSwitch setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"my_time_s" ofType:@"png"]] forState:UIControlStateNormal];
        [self.notiSwitch setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"my_time" ofType:@"png"]] forState:UIControlStateHighlighted];
        
    }
    else
    {
        //미설정
        [self.notiSwitch setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"my_time" ofType:@"png"]] forState:UIControlStateNormal];
        [self.notiSwitch setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"my_time_s" ofType:@"png"]] forState:UIControlStateHighlighted];
        
    }
    
    self.notiSwitch.tag = indexPath.row;
    [self.notiSwitch addTarget:self action:@selector(action:) forControlEvents:UIControlEventTouchUpInside];
    
    cell.accessoryView = self.notiSwitch;
    
    dicTime = nil;
    
    return cell;
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == 0) {
        MSG_NORMAL(@"저장 취소", @"확인");
    }
    else
    {
        MSG_NORMAL(@"저장 완료", @"확인");
        
        [self.navigationController popViewControllerAnimated:YES];
    }
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    //NSLog(@"%d", indexPath.row);
}


#pragma mark - UIActionSheet
-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex == TIME_SHEET_ARR.count+1){
        // 취소인 경우 return;
        return;
    }
    
//    NSLog(@"action sheet : %ld %ld %@", actionSheet.tag, (long)buttonIndex, [TIME_SHEET_ARR objectAtIndex:buttonIndex]);
    // 그 전에도 여기서 죽었겠다... Pickerview때문에 죽은게 아니라 superview.superview 라니... OMG
    NSIndexPath *path = [NSIndexPath indexPathForRow:actionSheet.tag inSection:0];
    
    UITableViewCell* cell = [self.tableView cellForRowAtIndexPath:path];
    
    cell.detailTextLabel.font = [UIFont systemFontOfSize:12.0];
    
    cell.detailTextLabel.text = [TIME_SHEET_ARR objectAtIndex:buttonIndex];

    [self.notiSwitch setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"my_time_s" ofType:@"png"]] forState:UIControlStateNormal];
    [self.notiSwitch setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"my_time" ofType:@"png"]] forState:UIControlStateHighlighted];
    
    cell.accessoryView = self.notiSwitch;
}

- (void) action: (id) sender
{
    self.notiSwitch = sender;
    
    self.notiSwitch.selected=!self.notiSwitch.selected;
    
    if (self.notiSwitch.selected)
    {
        NSString *title = nil;
        if(self.notiSwitch.tag == 0){
            title = @"시작시간";
        }else if(self.notiSwitch.tag == 1){
            title = @"종료시간";
        }
        UIActionSheet *actionSheet = [[UIActionSheet alloc] initWithTitle:title
                                                                 delegate:self
                                                        cancelButtonTitle:nil
                                                   destructiveButtonTitle:nil
                                                        otherButtonTitles:nil, nil];
        
        [actionSheet setTag:self.notiSwitch.tag]; // 0 = 시작, 1 = 종료
        
        for(NSString *timeStr in TIME_SHEET_ARR){
            [actionSheet addButtonWithTitle:timeStr];
        }
        [actionSheet addButtonWithTitle:@"취소"];
        [actionSheet setCancelButtonIndex:actionSheet.numberOfButtons-1];
        
        [actionSheet showInView:self.tabBarController.view];
        
        // 혹시 나중에 TabBar를 안쓰면 window에 바로 띄워야 함
//        [actionSheet showInView:self.view.window];
    }
    else
    {
        //UITableViewCell *cell = (UITableViewCell *)[self.notiSwitch superview];
        //cell.detailTextLabel.text = [CELL_DETAIL_MSG objectAtIndex:self.notiSwitch.tag];
    }
}

-(void)doSave:(id)sender
{
    NSDictionary* dicTime = [NSDictionary dictionaryWithObjectsAndKeys:
                             @"01", @"am 1:00",
                             @"02", @"am 2:00",
                             @"03", @"am 3:00",
                             @"04", @"am 4:00",
                             @"05", @"am 5:00",
                             @"06", @"am 6:00",
                             @"07", @"am 7:00",
                             @"08", @"am 8:00",
                             @"09", @"am 9:00",
                             @"10", @"am 10:00",
                             @"11", @"am 11:00",
                             @"12", @"am 12:00",
                             
                             @"13", @"pm 1:00",
                             @"14", @"pm 2:00",
                             @"15", @"pm 3:00",
                             @"16", @"pm 4:00",
                             @"17", @"pm 5:00",
                             @"18", @"pm 6:00",
                             @"19", @"pm 7:00",
                             @"20", @"pm 8:00",
                             @"21", @"pm 9:00",
                             @"22", @"pm 10:00",
                             @"23", @"pm 11:00",
                             @"24", @"pm 12:00",
                             nil];
    
    
    NSIndexPath *idxPath1 = [NSIndexPath indexPathForRow:0 inSection:0];
    NSIndexPath *idxPath2 = [NSIndexPath indexPathForRow:1 inSection:0];
    
    UITableViewCell *startTimeCell = [self.tableView cellForRowAtIndexPath:idxPath1];
    UITableViewCell *endTimeCell = [self.tableView cellForRowAtIndexPath:idxPath2];
    
    NSString *startTime = startTimeCell.detailTextLabel.text;
    NSString *endTime = endTimeCell.detailTextLabel.text;
    
    if ([startTime isEqualToString:@"미설정"] || [endTime isEqualToString:@"미설정"]) {
        MSG_NORMAL(@"알림 금지시간을 설정해주세요", @"확인");
    }
    else
    {
        NSString* str24StartTime = [dicTime objectForKey:startTime];
        NSString* str24EndTime = [dicTime objectForKey:endTime];
        
        int nStartTime = [str24StartTime intValue];
//        NSLog(@"str24StartTime : %d", nStartTime);
        
        int nEndTime = [str24EndTime intValue];
//        NSLog(@"str24EndTime : %d", nEndTime);
        
        // TODO : 시간 설정 병신같음 ㅋ 여기 수정좀 ㅋㅋㅋㅋ
        //당일 시간으로 놓고 봤을때 시작시간이 종료시간보다 크면.. 안됨.
        if (nStartTime > nEndTime) {
            MSG_NORMAL(@"알림 금지시간을 정확히 설정해주세요", @"확인");
        }
        else
        {
            [self notiSetTimeUpdate:str24StartTime andWithTime:str24EndTime];
            
            MSG_DELEGATE_BTN2(ALIM_TITLE_MSG, @"아니오", @"설정저장", nil, self);
        }
    }

    dicTime = nil;
}

- (NSNumber*)notiSetTimeUpdate:(NSString*)starthh andWithTime:(NSString*)endhh
{
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

@end
