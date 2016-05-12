#include <linux/fs.h>
#include <linux/init.h>
#include <linux/kernel.h>
#include <linux/mm.h>
#include <linux/hugetlb.h>
#include <linux/mman.h>
#include <linux/mmzone.h>
#include <linux/proc_fs.h>
#include <linux/quicklist.h>
#include <linux/seq_file.h>
#include <linux/swap.h>
#include <linux/vmstat.h>
#include <linux/atomic.h>
#include <linux/vmalloc.h>
#ifdef CONFIG_CMA
#include <linux/cma.h>
#endif
#include <asm/page.h>
#include <asm/pgtable.h>
#include <linux/module.h>

void __attribute__((weak)) arch_report_meminfo(struct seq_file *m)
{
}

static int meminfo_proc_show(struct seq_file *m, void *v)
{
    struct sysinfo i;
    struct task_struct *task;

/*
 * display in kilobytes.
 */
#define K(x) ((x) << (PAGE_SHIFT - 10))
    si_meminfo(&i);


    for_each_process(task)
            seq_printf(m,"%s, %d, %li\n", task->comm, task->pid, task->state);


    return 0;
#undef K
}

static int meminfo_proc_open(struct inode *inode, struct file *file)
{
    return single_open(file, meminfo_proc_show, NULL);
}

static const struct file_operations meminfo_proc_fops = {
    .open       = meminfo_proc_open,
    .read       = seq_read,
    .llseek     = seq_lseek,
    .release    = single_release,
};

static int __init DEGM_INIT(void)
{
    proc_create("DEGM", 0, NULL, &meminfo_proc_fops);
    printk(KERN_ALERT "\n\n\n\n200915754!\n");
        return 0;
}

static void DEGM_exit(void)
{
    printk(KERN_ALERT "Hasta la vista Baby!\n");
}

fs_initcall(DEGM_INIT);
module_exit(DEGM_exit);
