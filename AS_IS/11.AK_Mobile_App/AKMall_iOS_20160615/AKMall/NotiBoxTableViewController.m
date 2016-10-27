//
//  NotiBoxTableViewController.m
//  AKMall
//
//  Created by 한병일 on 2014. 6. 21..
//  Copyright (c) 2014년 한병일. All rights reserved.
//

#import "NotiBoxTableViewController.h"

@interface NotiBoxTableViewController ()

@end

@implementation NotiBoxTableViewController
@synthesize arrNotilist;
@synthesize pushid;

#define kroot_Item          @"root"
#define kNotiList_item      [[NSArray alloc] initWithObjects:@"alarm",  nil]
#define kNotiList_items     @"item"
#define kNotiListElements   [[NSDictionary alloc] initWithObjectsAndKeys:[[NSArray alloc] initWithObjects:@"PUSH_ID", @"EVENT_NAME",@"TITLE", @"SEND_DT",@"SHORT_URL", @"FACEBOOK", @"TWITTER" ,@"CONTENT" ,@"PAGE_URL", nil], @"alarm", nil]

#define NOTILIST_DETAIL_ATTREBUTE [[NSArray alloc] initWithObjects:kroot_Item, kNotiList_item, kNotiList_items, kNotiListElements, nil]

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
     UILabel *titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 320, 30)];
     titleLabel.textColor = [UIColor whiteColor];
     titleLabel.text = @"          알림 보관함";
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
     
     self.tableView.backgroundColor = [UIColor whiteColor];
     self.tableView.backgroundView = nil;
     [self.navigationController setNavigationBarHidden:NO animated:YES];
}
     
-(void) pushBackButton:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    if(self.arrNotilist == nil)
    {
        self.arrNotilist = [self notiList];
    }
    
    [self.tableView reloadData];
}

- (NSMutableArray *)notiList
{
    NSString *strURL = [NSString stringWithFormat:@"%@?act=getAlarmList", LibURL];
    
//    NSLog(@"notiList %@",strURL);
    
    XmlParser *xmlParser = [[XmlParser alloc] initParser:[NSURL URLWithString:strURL] andWithItems:NOTILIST_DETAIL_ATTREBUTE andWithParserType:(ParserType *)Parser3];
    
    if (xmlParser.parsedItems && [xmlParser.parsedItems count] > 0) {
        return [xmlParser.parsedItems objectAtIndex:0];
    }
    
    return nil;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if([self.arrNotilist count]==0){
        return 1;
    }
    
    return [self.arrNotilist count];
}

- (UITableViewCell *)tableView:(UITableView *)tView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier];
    }
    if([self.arrNotilist count]==0){
        cell.textLabel.text = @"보관된 알람이 없습니다";
        [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
        return cell;
    }
    
    NSMutableDictionary *items = [[NSMutableDictionary alloc] initWithDictionary:[self.arrNotilist objectAtIndex:indexPath.row]];
    cell.textLabel.text = [items objectForKey:[[kNotiListElements objectForKey:@"alarm"] objectAtIndex:2]];
    cell.detailTextLabel.text = [items objectForKey:[[kNotiListElements objectForKey:@"alarm"] objectAtIndex:3]];
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    
    //액세서리 뷰 이미지 바꾸기~
    UIImage* image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"arrow_black_right" ofType:@"png"]];
    UIImageView* imgView = [[UIImageView alloc] initWithImage:image];
    cell.accessoryView = imgView;
    
    return cell;
}

-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    UIImage *image = nil;
    UIImage *image_s = nil;
    
    //cell backgroundView로 바꿔주기
    if ((indexPath.row % 2) == 0)
	{
        image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"cate_listbg_a" ofType:@"png"]];
        image_s = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"cate_listbg_a_s" ofType:@"png"]];
        
	}
	else
	{
        image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"cate_listbg_b" ofType:@"png"]];
        image_s = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"cate_listbg_b_s" ofType:@"png"]];
        
	}
    
    UIImageView* imgView = [[UIImageView alloc] initWithImage:image];
    UIImageView* imgView_s = [[UIImageView alloc] initWithImage:image_s];
    
    cell.backgroundView = imgView;
    cell.selectedBackgroundView = imgView_s;
    
    //selected 됬을때...
    
    cell.textLabel.highlightedTextColor  = [UIColor blackColor];
    cell.detailTextLabel.highlightedTextColor  = [UIColor blackColor];
    cell.textLabel.font = [UIFont systemFontOfSize:15.0];
    cell.detailTextLabel.font = [UIFont systemFontOfSize:12.0];
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if([self.arrNotilist count]==0){
        return;
    }

    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:nil message:(NSString*)[[self.arrNotilist objectAtIndex:indexPath.row]objectForKey:@"CONTENT"] delegate:self cancelButtonTitle:@"취소"  otherButtonTitles:@"확인",nil];
    
    [alert show];
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == 1)
    {
//        [self performSegueWithIdentifier: @"NotiBoxWeb" sender: self];
//        self.pushid = nil;
        NSIndexPath *indexPath = [self.tableView indexPathForSelectedRow];
        NSString *shortUrl = [[self.arrNotilist objectAtIndex:indexPath.row] objectForKey:@"PAGE_URL"];
        if([shortUrl rangeOfString:@"http://"].location == NSNotFound)
        {
            shortUrl = [NSString stringWithFormat:@"%@%@",RootURL,shortUrl];
        }
        
        [self.delegate GoUrl:shortUrl];
        [self.navigationController popViewControllerAnimated:YES];
    }
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([segue.identifier isEqualToString:@"NotiBoxWeb"])
    {
        NSIndexPath *indexPath = [self.tableView indexPathForSelectedRow];
        NSString *shortUrl = [[self.arrNotilist objectAtIndex:indexPath.row] objectForKey:@"PAGE_URL"];
        if([shortUrl rangeOfString:@"http://"].location == NSNotFound)
        {
            shortUrl = [NSString stringWithFormat:@"%@%@",RootURL,shortUrl];
        }
        
        NotiBoxWebViewController *destViewController = segue.destinationViewController;
        destViewController.URL = shortUrl;
    }
}

@end
