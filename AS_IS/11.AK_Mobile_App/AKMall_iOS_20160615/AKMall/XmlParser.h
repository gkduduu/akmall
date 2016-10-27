//
//  XmlParser.h
//  test
//
//  Created by 한병일 on 2014. 6. 10..
//  Copyright (c) 2014년 한병일. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DDXML.h"
#import "DDXMLElementAdditions.h"

enum {
    Parser1 = 1,
    Parser2 = 2,
    Parser3 = 3,
    Parser4 = 4,
    Parser5 = 5
};

typedef NSUInteger ParserType;

@interface XmlParser : NSObject {
    NSMutableData *xmlData;
    NSURLConnection *itemConnection;
    NSMutableArray *parsedItems;
    NSArray *arrItems;
    BOOL isComplete;
    ParserType* nParserType;
}

@property (nonatomic, retain) NSMutableData *xmlData;
@property (nonatomic, retain) NSMutableArray *parsedItems;

-(id)initParser:(NSURL *)url andWithItems:(NSArray *)items;
-(id)initParser:(NSURL *)url andWithItems:(NSArray *)items  andWithParserType:(ParserType *)ParserType;
-(id)initOtherParser:(NSMutableURLRequest *)urlRequset  andWithItems:(NSArray *)items;
-(id)initOtherParser:(NSMutableURLRequest *)urlRequset  andWithItems:(NSArray *)items andWithParserType:(ParserType *)ParserType;
-(void)initRequest:(NSURLRequest *)req;

@end

