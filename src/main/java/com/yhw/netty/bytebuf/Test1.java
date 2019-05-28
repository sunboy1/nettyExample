package com.yhw.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * ByteBuf测试
 */
public class Test1 {

    public static void main(String[] args){
        //ByteBuf内存结构 读指针(readerIndex)  写指针(writeIndex)  容量(capacity) 三部分
        //设置ByteBuf的初始容量和最大容量(capacity),当容量超出100就会报错
        //初始化时readerIndex为0，每读取一个字节 readerIndex自增1
        //初始化时writerIndex为0，每写一个自己 writeIndex自增1
        //可以推论出当 两者(wIndex和wIndex)相等时，ByteBuf不可读【例如：初始化不可读，都为0】
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9,100);


        print("allocate ByteBuf(9, 100)", buffer);

        // write 方法改变写指针，写完之后写指针未到 capacity 的时候，buffer 仍然可写
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        print("writeBytes(1,2,3,4)", buffer);

        // write 方法改变写指针，写完之后写指针未到 capacity 的时候，buffer 仍然可写, 写完 int 类型之后，写指针增加4
        buffer.writeInt(12);
        print("writeInt(12)", buffer);

        // write 方法改变写指针, 写完之后写指针等于 capacity 的时候，buffer 不可写
        buffer.writeBytes(new byte[]{5});
        print("writeBytes(5)", buffer);

        // write 方法改变写指针，写的时候发现 buffer 不可写则开始扩容，扩容之后 capacity 随即改变
        buffer.writeBytes(new byte[]{6});
        print("writeBytes(6)", buffer);

        // get 方法不改变读写指针
        System.out.println("getByte(3) return: " + buffer.getByte(3));
        System.out.println("getShort(3) return: " + buffer.getShort(3));
        System.out.println("getInt(3) return: " + buffer.getInt(3));
        print("getByte()", buffer);


        // set 方法不改变读写指针
        buffer.setByte(buffer.readableBytes() + 1, 0);
        print("setByte()", buffer);

        // read 方法改变读指针
        byte[] dst = new byte[buffer.readableBytes()];
        buffer.readBytes(dst);
        print("readBytes(" + dst.length + ")", buffer);

    }

    public static void print(String action,ByteBuf buffer){
        System.out.println("after ===========" + action + "============");
        System.out.println("capacity(): " + buffer.capacity());
        System.out.println("maxCapacity(): " + buffer.maxCapacity());
        System.out.println("readerIndex(): " + buffer.readerIndex());
        System.out.println("readableBytes(): " + buffer.readableBytes());
        System.out.println("isReadable(): " + buffer.isReadable());
        System.out.println("writerIndex(): " + buffer.writerIndex());
        System.out.println("writableBytes(): " + buffer.writableBytes());
        System.out.println("isWritable(): " + buffer.isWritable());
        System.out.println("maxWritableBytes(): " + buffer.maxWritableBytes());
        System.out.println();
    }



    /**
     * 读写指针API操作
     * 1.重置读指针
     */
    private void reSetIndex(){
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(9,100);

        //代码1 等价 代码2  优先使用代码2 【使用两个方法可以避免创建 没必要的对象】
        //代码1
        int readerIndex = byteBuf.readerIndex();//返回当前可读指针位置
        byteBuf.readerIndex(readerIndex);//把可读指针定位到目标位置

        //代码2
        byteBuf.markReaderIndex();//把当前的读指针保存
        byteBuf.resetReaderIndex();//把当前的读指针恢复到 之前保存读指针的位置


        //===写指针
        //返回当前写指针的位置
        byteBuf.writerIndex();
        //设置写指针的位置
        byteBuf.writerIndex(8);
    }

    /**
     * 2.读写API
     */
    private void readAndWriteApi(){
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(9,100);

        //slice() duplicate() copy() 都返回的是一个新ByteBuf对象

        //从原始ByteBuf中截取一段，  从readerIndex到writeIndex  最大容量为原始ByteBuf的readableBytes()[当前可读字节长度]
        byteBuf.slice();

        //把整个ByteBuf都截取出来，包括所以数据，指针信息
        byteBuf.duplicate();

        //slice() 方法与 duplicate() 方法的相同点是：
        // 底层内存以及引用计数与原始的 ByteBuf 共享，也就是说经过 slice()
        // 或者 duplicate() 返回的 ByteBuf 调用 write 系列方法都会影响到 原始的 ByteBuf，但是它们都维持着与原始
        // ByteBuf 相同的内存引用计数和不同的读写指针

        //slice() 方法与 duplicate() 不同点就是：
        // slice() 只截取从 readerIndex 到 writerIndex 之间的数据，
        // 它返回的 ByteBuf 的最大容量被限制到 原始 ByteBuf 的 readableBytes(),
        // 而 duplicate() 是把整个 ByteBuf 都与原始的 ByteBuf 共享

//        slice() 方法与 duplicate() 方法不会拷贝数据，它们只是通过改变读写指针来改变读写的行为，
//        而最后一个方法 copy() 会直接从原始的 ByteBuf 中拷贝所有的信息，包括读写指针以及底层对应的数据，
//        因此，往 copy() 返回的 ByteBuf 中写数据不会影响到原始的 ByteBuf


//        slice() 和 duplicate() 不会改变 ByteBuf 的引用计数，所以原始的 ByteBuf 调用 release() 之后发现引用计数为零，
//        就开始释放内存，调用这两个方法返回的 ByteBuf 也会被释放，这个时候如果再对它们进行读写，就会报错。
//        因此，我们可以通过调用一次 retain() 方法 来增加引用，表示它们对应的底层的内存多了一次引用，
//        引用计数为2，在释放内存的时候，需要调用两次 release() 方法，将引用计数降到零，才会释放内存


//        这三个方法均维护着自己的读写指针，与原始的 ByteBuf 的读写指针无关，相互之间不受影响

        byteBuf.copy();

        //注意不要多次释放
        // retainedSlice 等价于
//        slice().retain();

        // retainedDuplicate() 等价于
//        duplicate().retain()
    }
}
