package mr_sort.map3;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //job初始化
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","file:///");
        Job job = Job.getInstance(conf);
        job.setJarByClass(App.class);

        //输入
        FileInputFormat.addInputPath(job,new Path("/home/centos/txt/a.txt"));


        //map, reduce
        job.setMapperClass(Map1.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setPartitionerClass(Partion1.class);//map后的分隔器

        job.setNumReduceTasks(4);

        job.setReducerClass(Red1.class);


        //输出
        FileOutputFormat.setOutputPath(job,new Path("/home/centos/txt/a-mpr-part22"));
        //等待工作完成
        //等待工作完成
        boolean flag = job.waitForCompletion(true);

//        flag=false;
        if(flag){//第一步工作完成
            Job job2 = Job.getInstance(conf);
            job2.setJarByClass(App.class);

            //输入
            FileInputFormat.addInputPath(job2,new Path("/home/centos/txt/a-mpr-part22"));


            //map, reduce
            job2.setMapperClass(Map2.class);
            job2.setMapOutputKeyClass(Text.class);
            job2.setMapOutputValueClass(IntWritable.class);

            job2.setReducerClass(Red1.class);
//            job.setNumReduceTasks(4);//多个reducer, 使得有空任务）

            //输出
            FileOutputFormat.setOutputPath(job2,new Path("/home/centos/txt/a-mpr-part22-res"));
            //等待工作完成
            job2.waitForCompletion(true);
        }

    }
}
