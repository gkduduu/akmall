//
//  SoundSettingTableViewController.m
//  AKMall
//
//  Created by 한병일 on 2014. 6. 23..
//  Copyright (c) 2014년 한병일. All rights reserved.
//

#import "SoundSettingTableViewController.h"

@interface SoundSettingTableViewController ()

@end

@implementation SoundSettingTableViewController
@synthesize list;
@synthesize baseURL;

#define FONT_COLOR_BLACK    [UIColor colorWithWhite:0.200 alpha:1.000]

SystemSoundID soundID;

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
    
    NSArray *arr = [[NSArray alloc] initWithObjects:@" 무음", @" 진동", @" 알림소리", nil];
    
    self.list = arr;
    
    CGRect tableViewFrame = CGRectMake(self.view.frame.origin.x, self.view.frame.origin.y, self.view.frame.size.width, 150);;
    self.tableView = [[UITableView alloc] initWithFrame:tableViewFrame style:UITableViewStyleGrouped];
    self.tableView.autoresizingMask = (UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight);
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleSingleLine;
    
    self.tableView.scrollEnabled = NO;
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    self.tableView.backgroundColor = UIColorFromRGB(0XE5E5E5);
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.tableView.separatorColor =  UIColorFromRGB(0XE5E5E5);
    
    
    UILabel * lblTitle1 = [[UILabel alloc] initWithFrame:CGRectMake(12, 180, 310, 80)];
    [lblTitle1 setFont:[UIFont boldSystemFontOfSize:15.0]];
    lblTitle1.text = @"알림 소리 진동 설정 메뉴에 원하는 기능을 체크해야만 서비스를 받으실 수 있습니다. 또한 폰 설정에 기능 체크를 하지 않으시면 서비스를 받으실 수 없습니다.";
    lblTitle1.numberOfLines = 4;
    lblTitle1.backgroundColor = [UIColor clearColor];
    lblTitle1.textAlignment = NSTextAlignmentLeft;
    lblTitle1.textColor = FONT_COLOR_BLACK;
    
    [self.view addSubview:lblTitle1];
    
    [self.tableView reloadData];

    UILabel *titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 320, 30)];
    titleLabel.textColor = [UIColor whiteColor];
    titleLabel.text = @"     알림 소리/진동 설정";
    //titleLabel.textAlignment = NSTextAlignmentCenter;
    [self.navigationItem setTitleView:titleLabel];
    self.tableView.backgroundView = nil;
    
    [[UINavigationBar appearance] setBackgroundImage:[UIImage imageNamed:@"topbg.png"] forBarMetrics:UIBarMetricsDefault];
    
    UIImage *backImage = [UIImage imageNamed:@"btn_back.png"];
    UIButton *backButton = [UIButton buttonWithType:UIButtonTypeCustom];
    backButton.frame = CGRectMake(0, 0, backImage.size.width, backImage.size.height);
    
    [backButton setImage:backImage forState:UIControlStateNormal];
    UIBarButtonItem *backBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:backButton];
    [backButton addTarget:self action:@selector(pushBackButton:)    forControlEvents:UIControlEventTouchUpInside];
    self.navigationItem.hidesBackButton = YES;
    self.navigationItem.leftBarButtonItem = backBarButtonItem;
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];
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
    
    return [list count];
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    UIImage *image = nil;
    
    if (indexPath.row == 0) {
        
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
    
    UIImageView *imgView = [[UIImageView alloc] initWithImage:image];
    
    cell.backgroundView = imgView;
}

- (UITableViewCell *)tableView:(UITableView *)table cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [table dequeueReusableCellWithIdentifier:@"NotiSetCell"];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:@"NotiSoundSetCell"];
        
    }
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    NSUInteger row = [indexPath row];
    
    cell.textLabel.text = [list objectAtIndex:row];
    cell.textLabel.backgroundColor = [UIColor clearColor];
    cell.textLabel.textColor = FONT_COLOR_BLACK;
    
    int nNotiSound = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"notiSound"];
    
    //설정값이랑 같으면 표시
    //디폴트는 무음.
    if ([indexPath row] == nNotiSound)
    {
        cell.accessoryType = UITableViewCellAccessoryNone;
        UIImage* image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"my_check" ofType:@"png"]];
        UIImageView* imgView = [[UIImageView alloc] initWithImage:image];
        
        cell.accessoryView = imgView;
    }
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell;
    
    //모든 행을 초기화 시키기
    for (int i=0; i < [self.list count]; i++) {
        cell = [self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:i inSection:0]];
        cell.accessoryType = UITableViewCellAccessoryNone;
        cell.accessoryView = nil;
        
    }
    
    //다시 셋팅하기
    cell = [self.tableView cellForRowAtIndexPath:indexPath];
    cell.accessoryType = UITableViewCellAccessoryNone;
    //액세서리 뷰 이미지 바꾸기~
    
    UIImage* image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"my_time_s" ofType:@"png"]];
    
    UIImageView* imgView = [[UIImageView alloc] initWithImage:image];
    
    cell.accessoryView = imgView;
    
    if (indexPath.row == 0)
    {
        [[NSUserDefaults standardUserDefaults] setInteger:0  forKey:@"notiSound"];
    }
    else if (indexPath.row == 1)
    {
        AudioServicesPlaySystemSound(kSystemSoundID_Vibrate);
        
        [[NSUserDefaults standardUserDefaults] setInteger:1  forKey:@"notiSound"];
    }
    else if (indexPath.row == 2)
    {
        //2011.08.03 음원이 없어서 예비로 넣은거임 추후 법적 문제 ~
        NSString* sndPath = [[NSBundle mainBundle] pathForResource:@"sound" ofType:@"WAV"];
        //NSLog(@"sndPath : %@", sndPath);
        if (sndPath) {
            //음원 경로
            NSString *path = [NSString stringWithFormat:@"%@%@", [[NSBundle mainBundle] resourcePath],   @"/sound.WAV"];
            
            NSURL *filePath = [NSURL fileURLWithPath:path isDirectory:NO];
            
            CFURLRef url = (__bridge CFURLRef)filePath;
            AudioServicesCreateSystemSoundID(url, &soundID);
            
            //음원 재생
            AudioServicesPlaySystemSound(soundID);
        }
        else
        {
            AudioServicesPlaySystemSound(kSystemSoundID_Vibrate);
        }
        
        [[NSUserDefaults standardUserDefaults] setInteger:2  forKey:@"notiSound"];
        
    }
    
    
    
    
    
    
}


@end
