//
//  AkNotiListBoxViewController.h
//  AppAkPlaza_iOS
//
//  Created by uniwis on 11. 6. 13..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UWNavigation.h"
#import "colorUtil.h"
#import "fontSizeUtil.h"
#import "AkNotiModel.h"

@interface AkNotiListBoxViewController : UWNavigation <baseModelDelegate,UITableViewDelegate, UITableViewDataSource, UITabBarControllerDelegate> {
    
    UITableView *tableView;
    
    NSArray					*arrNotilist;
    
    int nPageNum;
    
    UIActivityIndicatorView *activityView;
    
    
    
    
} 

@property (nonatomic, retain) UITableView *tableView;
@property (nonatomic, retain) NSArray *arrNotilist;
@property int nPageNum;
@property (nonatomic, retain) NSString* pushid;
@property (nonatomic, retain) NSString* bc;
@property (nonatomic, retain) AkNotiModel *oModel;
@property (nonatomic, retain) NSString* arrNotiUrl;//p65458 20130620 add


@property (nonatomic, retain) UIActivityIndicatorView *activityView;



- (UITableViewCell *)tableViewCellWithReuseIdentifier:(NSString *)identifier;
- (void)configureCell:(UITableViewCell *)cell forIndexPath:(NSIndexPath *)indexPath;
- (void)pushView:(NSString *)url; //p65458


@end
