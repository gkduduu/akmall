//
//  NotiBoxTableViewController.h
//  AKMall
//
//  Created by 한병일 on 2014. 6. 21..
//  Copyright (c) 2014년 한병일. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Define.h"
#import "XmlParser.h"
#import "NotiBoxWebViewController.h"

@protocol NotiBoxTableViewControllerDelegate <NSObject>

@optional
-(void)GoUrl:(NSString*)URL;

@end
@interface NotiBoxTableViewController : UITableViewController

@property (nonatomic, retain) NSString* pushid;
@property (nonatomic, retain) NSArray *arrNotilist;
@property (nonatomic, weak) id <NotiBoxTableViewControllerDelegate> delegate;

@end

